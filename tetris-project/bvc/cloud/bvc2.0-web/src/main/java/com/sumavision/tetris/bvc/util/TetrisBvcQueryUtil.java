package com.sumavision.tetris.bvc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
//import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.config.ServerProps;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.demand.GroupDemandPO;
import com.sumavision.tetris.bvc.business.group.function.GroupFunctionService;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioPO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineContentType;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;

/**
 * 查询工具类<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月10日 下午1:19:21
 */
@Service
public class TetrisBvcQueryUtil {
	
	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private GroupFunctionService groupFunctionService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	/** 根据dstId查找分页任务 */
	public PageTaskPO queryPageTaskById(Collection<PageTaskPO> tasks, String dstId) {
		if(tasks == null) return null;
		for(PageTaskPO task : tasks){
			if(dstId.equals(task.getDstId())){
				return task;
			}
		}
		return null;
	}
	
	public AgendaPO queryAgendaById(Collection<AgendaPO> agendas, Long id){
		for(AgendaPO agenda : agendas){
			if(id.equals(agenda.getId())){
				return agenda;
			}
		}
		return null;
	}
	
	public CommonForwardPO queryForwardByUuid(Collection<CommonForwardPO> forwards, String uuid){
		if(uuid == null) return null;
		for(CommonForwardPO forward : forwards){
			if(uuid.equals(forward.getUuid())){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 查找源或目的在memberId列表中的转发<br/>
	 * @param forwards 转发列表
	 * @param acceptMemberIds 成员id列表
	 * @param type 转发业务类型，如果为null则查全部类型
	 * @param executeStatus 转发执行状态，如果为null则查全部状态
	 * @throws Exception 
	 * @return Set<CommandGroupForwardPO> needForwards 符合条件的转发列表
	 */
	public Set<CommandGroupForwardPO> queryForwardsByMemberIds(
			Collection<CommandGroupForwardPO> forwards, Collection<Long> memberIds, ForwardBusinessType type, ExecuteStatus executeStatus) {
		Set<CommandGroupForwardPO> needForwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupForwardPO forward : forwards){
			if(type==null || type.equals(forward.getForwardBusinessType())){
				if(executeStatus==null || executeStatus.equals(forward.getExecuteStatus())){
					if(memberIds.contains(forward.getDstMemberId())){
						needForwards.add(forward);
					}else if(memberIds.contains(forward.getSrcMemberId())){
						needForwards.add(forward);
					}
				}
			}
		}
		return needForwards;
	}
	
	/**
	 * 根据源成员和业务类型查找转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午5:21:50
	 * @param forwards 转发列表
	 * @param srcMemberIds
	 * @param type 转发业务类型，如果为null则查全部类型
	 * @return
	 */
	public Set<CommonForwardPO> queryForwardsBySrcmemberIds(
			Collection<CommonForwardPO> forwards, Collection<Long> srcMemberIds, BusinessInfoType type) {
		Set<CommonForwardPO> needForwards = new HashSet<CommonForwardPO>();
		for(CommonForwardPO forward : forwards){
			if(type==null || type.equals(forward.getBusinessInfoType())){
				if(srcMemberIds.contains(forward.getSrcMemberId())){
					needForwards.add(forward);
				}
			}
		}
		return needForwards;
	}
	
	/**
	 * 根据目的成员和业务类型查找转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月9日 上午11:44:49
	 * @param forwards
	 * @param dstMemberIds
	 * @param type 转发业务类型，如果为null则查全部类型
	 * @param executeStatus 转发执行状态，如果为null则查全部状态
	 * @return
	 */
	public List<CommandGroupForwardPO> queryForwardsByDstmemberIds(
			Collection<CommandGroupForwardPO> forwards, Collection<Long> dstMemberIds, ForwardBusinessType type, ExecuteStatus executeStatus) {
		List<CommandGroupForwardPO> needForwards = new ArrayList<CommandGroupForwardPO>();
		for(CommandGroupForwardPO forward : forwards){
			if(type==null || type.equals(forward.getForwardBusinessType())){
				if(executeStatus==null || executeStatus.equals(forward.getExecuteStatus())){
					if(dstMemberIds.contains(forward.getDstMemberId())){
						needForwards.add(forward);
					}
				}
			}
		}
		return needForwards;
	}
	
	/**
	 * @Title: 具备执行条件的转发：执行状态为UNDONE，源和目的成员都已经CONNECT<br/>
	 * @description 搭配 CommandCommonServiceImpl 的 whetherCanBeDone 方法一起使用
	 * @param members 必须覆盖forwards转发所涉及到的所有成员；建议从forward对应的group中get得到，避免脏读
	 * @param forwards 转发列表
	 * @throws Exception 
	 * @return Set<CommandGroupForwardPO> needForwards 具备执行条件的转发列表
	 */
	public Set<CommandGroupForwardPO> queryForwardsReadyToBeDone(Collection<CommandGroupMemberPO> members, Collection<CommandGroupForwardPO> forwards) {
		Set<CommandGroupForwardPO> needForwards = new HashSet<CommandGroupForwardPO>();
		Map<Long, CommandGroupMemberPO> map = membersSetToMap(members);
		for(CommandGroupForwardPO forward : forwards){
			Long srcMemberId = forward.getSrcMemberId();
			Long dstMemberId = forward.getDstMemberId();
			//执行状态为UNDONE，源和目的成员都已经CONNECT
			if(forward.getExecuteStatus().equals(ExecuteStatus.UNDONE)
					&& map.get(srcMemberId).getMemberStatus().equals(MemberStatus.CONNECT)
					&& map.get(dstMemberId).getMemberStatus().equals(MemberStatus.CONNECT)){
				//如果是协同指挥转发（取消）
//				if(ForwardBusinessType.COOPERATE_COMMAND.equals(forward.getForwardBusinessType())){
//					if(map.get(srcMemberId).getCooperateStatus().equals(MemberStatus.CONNECT)){
//						needForwards.add(forward);
//					}
//				}else{
					needForwards.add(forward);
//				}
			}
		}
		return needForwards;
	}
	
	/**
	 * 具备执行条件，且没有互斥业务的转发：<br/>
	 * 执行状态为UNDONE，源和目的成员都已经CONNECT，<br/>
	 * 该转发没有因为指挥暂停、专向指挥、静默操作而停止，才可以被执行。<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午10:49:19
	 * @param members 必须覆盖forwards转发所涉及到的所有成员；建议从forward对应的group中get得到，避免脏读
	 * @param forwards 转发列表
	 * @return
	 */
	public Set<CommandGroupForwardPO> queryForwardsReadyAndCanBeDone(Collection<CommandGroupMemberPO> members, Collection<CommandGroupForwardPO> forwards) {
		Set<CommandGroupForwardPO> needForwards = new HashSet<CommandGroupForwardPO>();
		Set<CommandGroupForwardPO> readyForwards = queryForwardsReadyToBeDone(members, forwards);
		for(CommandGroupForwardPO forward : readyForwards){
			if(commandCommonServiceImpl.whetherCanBeDone(forward)){
				needForwards.add(forward);
			}
		}
		return needForwards;
	}
	
	//把Set<CommandGroupMemberPO>以id为key转为map，供queryForwardsNeedToBeDone调用
	private Map<Long, CommandGroupMemberPO> membersSetToMap (Collection<CommandGroupMemberPO> members) {
		Map<Long, CommandGroupMemberPO> map = new HashMap<Long, CommandGroupMemberPO>();
		if (members == null) {
			return map;
		}
		for(CommandGroupMemberPO member : members){
			map.put(member.getId(), member);
		}
		return map;
	}
	
	/**
	 * @Title: 根据dstVideoBundleId查找转发<br/>
	 * @param Set<CommandGroupForwardPO> forwards 转发列表
	 * @param String dstVideoBundleId
	 * @throws Exception 
	 * @return CommandGroupForwardPO forward 转发
	 */
	public CommandGroupForwardPO queryForwardByDstVideoBundleId(Collection<CommandGroupForwardPO> forwards, String dstVideoBundleId) {
		for(CommandGroupForwardPO forward : forwards){
			if(dstVideoBundleId.equals(forward.getDstVideoBundleId())){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 根据dstAudioBundleId查找转发<br/>
	 * @param Set<CommandGroupForwardPO> forwards 转发列表
	 * @param String dstAudioBundleId
	 * @throws Exception 
	 * @return CommandGroupForwardPO forward 转发
	 */
	public CommandGroupForwardPO queryForwardByDstAudioBundleId(Collection<CommandGroupForwardPO> forwards, String dstAudioBundleId) {
		for(CommandGroupForwardPO forward : forwards){
			if(dstAudioBundleId.equals(forward.getDstAudioBundleId())){
				return forward;
			}
		}
		return null;
	}
	
	/**
	 * 根据源和目的成员，查找一个转发<br/>
	 * <p>快速查转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午10:02:27
	 * @param forwards
	 * @param srcMemberId 如果为空，则忽略该参数
	 * @param dstMemberId 如果为空，则忽略该参数
	 * @return
	 */
	public CommandGroupForwardPO queryForwardBySrcAndDstMemberId(Collection<CommandGroupForwardPO> forwards, Long srcMemberId, Long dstMemberId) {
		for(CommandGroupForwardPO forward : forwards){
			if(srcMemberId==null || srcMemberId.equals(forward.getSrcMemberId())){
				if(dstMemberId==null || dstMemberId.equals(forward.getDstMemberId())){
					return forward;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据bundleId查找播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午2:58:41
	 * @param players
	 * @param bundleId
	 * @return
	 */
	public CommandGroupUserPlayerPO queryPlayerByBundleId(Collection<CommandGroupUserPlayerPO> players, String bundleId) {
		for(CommandGroupUserPlayerPO player : players){
			if(player.getBundleId().equals(bundleId)){
				return player;
			}
		}
		return null;
	}
	
	/**
	 * 根据号码code查找播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午1:08:54
	 * @param players
	 * @param code
	 * @return
	 */
	public CommandGroupUserPlayerPO queryPlayerByCode(Collection<CommandGroupUserPlayerPO> players, String code) {
		for(CommandGroupUserPlayerPO player : players){
			if(player.getCode().equals(code)){
				return player;
			}
		}
		return null;
	}
	
	/**
	 * 根据位置索引查找播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午1:24:06
	 * @param players
	 * @param locationIndex
	 * @return
	 */
	public CommandGroupUserPlayerPO queryPlayerByLocationIndex(List<CommandGroupUserPlayerPO> players, int locationIndex) {
		for(CommandGroupUserPlayerPO player : players){
			if(player.getLocationIndex() == locationIndex){
				return player;
			}
		}
		return null;
	}
	
	/** 根据id查找上屏方案中的分屏 */
	public CommandGroupDecoderScreenPO queryScreenById(List<CommandGroupDecoderScreenPO> screens, Long id) {
		if(screens == null) return null;
		for(CommandGroupDecoderScreenPO screen : screens){
			if(screen.getId().equals(id)){
				return screen;
			}
		}
		return null;
	}

	/**
	 * 根据业务类型查找播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月29日 上午9:07:33
	 * @param players
	 * @param type
	 * @return
	 */
	public CommandGroupUserPlayerPO queryPlayerByPlayerBusinessType(List<CommandGroupUserPlayerPO> players, PlayerBusinessType type) {
		for(CommandGroupUserPlayerPO player : players){
			if(player.getPlayerBusinessType().equals(type)){
				return player;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 通过转发和目标成员，查找该转发的目标播放器<br/>
	 * <p>快速查播放器，可以在 queryForwardBySrcAndDstMemberId() 方法后面使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午10:16:17
	 * @param forward 为空则直接返回null
	 * @param dstMember
	 * @return
	 */
	public CommandGroupUserPlayerPO queryPlayerByForwardAndDstMember(CommandGroupForwardPO forward, CommandGroupMemberPO dstMember) {
		if(forward == null) return null;
		for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
			if(player.getBundleId().equals(forward.getDstVideoBundleId())){
				return player;
			}
		}
		return null;
	}

	/**
	 * 根据id列表查找指挥转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 上午9:53:23
	 * @param demands
	 * @param ids
	 * @return
	 */
	public List<GroupDemandPO> queryForwardDemandsByIds(
			List<GroupDemandPO> demands, List<Long> ids) {
		List<GroupDemandPO> needDemands = new ArrayList<GroupDemandPO>();
		for(GroupDemandPO demand : demands){
			if(ids.contains(demand.getId())){
				needDemands.add(demand);
			}
		}
		return needDemands;
	}
	
	/**
	 * 根据目的成员和转发业务类型查找指挥转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月14日 下午4:22:21
	 * @param demands 指挥转发点播列表
	 * @param dstMemberIds
	 * @param type 转发业务类型，如果为null则查全部类型
	 * @param executeStatus 转发执行状态，如果为null则查全部状态
	 * @return
	 */
	public List<CommandGroupForwardDemandPO> queryForwardDemandsByDstmemberIds(
			List<CommandGroupForwardDemandPO> demands, List<Long> dstMemberIds, ForwardDemandBusinessType type, ForwardDemandStatus executeStatus) {
		List<CommandGroupForwardDemandPO> needDemands = new ArrayList<CommandGroupForwardDemandPO>();
		for(CommandGroupForwardDemandPO demand : demands){
			if(type==null || type.equals(demand.getDemandType())){
				if(executeStatus==null || executeStatus.equals(demand.getExecuteStatus())){
					if(dstMemberIds.contains(demand.getDstMemberId())){
						needDemands.add(demand);
					}
				}
			}
		}
		return needDemands;
	}
	
	/**
	 * 根据源号码和目的号码查询转发调度<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午2:00:49
	 * @param demands
	 * @param srcCodes
	 * @param dstCodes
	 * @return
	 */
	public GroupDemandPO queryForwardDemandBySrcAndDstCode(
			List<GroupDemandPO> demands, String srcCode, String dstCode) {
		for(GroupDemandPO demand : demands){
			if(srcCode.equals(demand.getSrcCode()) && dstCode.equals(demand.getDstCode())){
				return demand;
			}
		}
		return null;
	}
	
	/**
	 * @Title: 根据dstVideoBundleId查找转发点播<br/>
	 * @param List<CommandGroupForwardDemandPO> forwards 转发点播列表
	 * @param String dstVideoBundleId
	 * @throws Exception 
	 * @return CommandGroupForwardDemandPO demand 转发点播
	 */
	public CommandGroupForwardDemandPO queryForwardByDstVideoBundleId(List<CommandGroupForwardDemandPO> demands, String dstVideoBundleId) {
		for(CommandGroupForwardDemandPO demand : demands){
			if(dstVideoBundleId.equals(demand.getDstVideoBundleId())){
				return demand;
			}
		}
		return null;
	}

	/**
	 * @Title: 查找指挥组中的档位参数<br/>
	 * @param CommandGroupPO group 指挥组
	 * @throws Exception 
	 * @return CommandGroupAvtplGearsPO 档位参数
	 */
	public CommandGroupAvtplGearsPO queryCurrentGear(CommandGroupPO group) {
		CommandGroupAvtplPO avtpl = group.getAvtpl();
		Set<CommandGroupAvtplGearsPO> gears = avtpl.getGears();
		if(group.getCurrentGearLevel() == null){
			group.setCurrentGearLevel(GearsLevel.LEVEL_3);
			commandGroupDao.save(group);
		}
		for(CommandGroupAvtplGearsPO gear:gears){
			if(gear.getLevel().equals(group.getCurrentGearLevel())){
				return gear;
			}
		}
		return null;
	}
	
	/**
	 * 根据id查找成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 上午9:48:39
	 * @param members
	 * @param id
	 * @return
	 */
	public GroupMemberPO queryMemberById(Collection<GroupMemberPO> members, Long id){
		for(GroupMemberPO member : members){
			if(id.equals(member.getId())){
				return member;
			}
		}
		return null;
	}
	
	/**
	 * 根据originId和类型查找成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:44:19
	 * @param members
	 * @param originId
	 * @param groupMemberType
	 * @return
	 */
	public GroupMemberPO queryMemberByOriginIdAndGroupMemberType(Collection<GroupMemberPO> members, String originId, GroupMemberType groupMemberType){
		for(GroupMemberPO member : members){
			if(originId.equals(member.getOriginId()) && groupMemberType.equals(member.getGroupMemberType())){
				return member;
			}
		}
		return null;
	}

	/**
	 * 根据userId查找成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:44:19
	 * @param members
	 * @param userId
	 * @return
	 */
	public GroupMemberPO queryMemberByUserId(Collection<GroupMemberPO> members, Long userId){
		for(GroupMemberPO member : members){
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())
					&& userId.toString().equals(member.getOriginId())){
				return member;
			}
		}
		return null;
	}

	/**
	 * 根据userId列表查找成员列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月14日 下午3:28:50
	 * @param members
	 * @param userIds
	 * @return
	 */
	public List<GroupMemberPO> queryMembersByUserIds(Collection<GroupMemberPO> members, List<Long> userIds){
		List<GroupMemberPO> target = new ArrayList<GroupMemberPO>();
		for(GroupMemberPO member : members){
			if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())) continue;
			try{
				//OriginId可能不是数字
				Long memberUserId = Long.parseLong(member.getOriginId());
				if(userIds.contains(memberUserId)){
					target.add(member);
				}
			}catch(Exception e){}			
		}
		return target;
	}
	
	public List<GroupMemberPO> queryMembersByIds(Collection<GroupMemberPO> members, List<Long> memberIds){
		List<GroupMemberPO> target = new ArrayList<GroupMemberPO>();
		for(GroupMemberPO member : members){
			try{
				if(memberIds.contains(member.getId())){
					target.add(member);
				}
			}catch(Exception e){}			
		}
		return target;
	}

	/**
	 * 根据成员状态以及协同状态查询成员列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月19日 上午11:47:08
	 * @param members 是否进入，null则忽略
	 * @param memberStatus 是否在协同/发言，null则忽略
	 * @param cooperateStatus
	 * @return
	 */
	public List<CommandGroupMemberPO> queryMembersByMemberStatusAndCooperateStatus(
			Collection<CommandGroupMemberPO> members,
			MemberStatus memberStatus,
			MemberStatus cooperateStatus){
		List<CommandGroupMemberPO> target = new ArrayList<CommandGroupMemberPO>();
		for(CommandGroupMemberPO member : members){
			if(memberStatus == null){
				if(cooperateStatus == null){
					target.add(member);
				}else if(cooperateStatus.equals(member.getCooperateStatus())){
					target.add(member);
				}
			}else if(memberStatus.equals(member.getMemberStatus())){
				if(cooperateStatus == null){
					target.add(member);
				}else if(cooperateStatus.equals(member.getCooperateStatus())){
					target.add(member);
				}
			}
		}
		return target;
	}
	
	/**
	 * 从指挥中查找主席<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月4日 下午4:33:26
	 * @param group
	 * @return
	 */
	@Deprecated
	public CommandGroupMemberPO queryChairmanMember(CommandGroupPO group){
		for(CommandGroupMemberPO member : group.getMembers()){
			if(member.isAdministrator()){
				return member;
			}
		}
		return null;
	}
	public GroupMemberPO queryChairmanMember(Collection<GroupMemberPO> members){
		for(GroupMemberPO member : members){
			if(member.getIsAdministrator()){
				return member;
			}
		}
		return null;
	}
	
	public List<GroupMemberPO> queryConnectMembers(Collection<GroupMemberPO> members){
		List<GroupMemberPO> target = new ArrayList<GroupMemberPO>();
		for(GroupMemberPO member : members){
			if(GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())){
				target.add(member);
			}
		}
		return target;
	}
	
	public CommandGroupUserLayoutShemePO querySchemeByPlayerSplitLayout(CommandGroupUserInfoPO userInfo, PlayerSplitLayout splitLayout){
		for(CommandGroupUserLayoutShemePO scheme : userInfo.getLayoutSchemes()){
			if(scheme.getPlayerSplitLayout().equals(splitLayout)){
				return scheme;
			}
		}
		return null;
	}
	
	public CommandGroupRecordFragmentPO queryFragmentBySrcMemberId(CommandGroupRecordPO record, Long srcMemberId){
		for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
			if(fragment.getSrcMemberId().equals(srcMemberId)){
				return fragment;
			}
		}
		return null;
	}
	
	public CommandGroupRecordFragmentPO queryFragmentByInfo(CommandGroupRecordPO record, String info){
		for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
			if(fragment.getInfo().equals(info)){
				return fragment;
			}
		}
		return null;
	}
	
	/**
	 * 查询members列表对应的UserBO列表，目前只支持用户成员<br/>
	 * <p>用户在线情况按照 QT_ZK 查询</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 上午9:40:02
	 * @param members
	 * @return
	 */
	public List<UserBO> queryUsersByMembers(Collection<GroupMemberPO> members){
		List<Long> userIdList = new ArrayList<Long>();
		for(GroupMemberPO member : members){
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				userIdList.add(Long.parseLong(member.getOriginId()));
			}
		}
		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
		List<UserBO> commandUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		return commandUserBos;
	}
	
	/**
	 * 根据id查询group<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月9日 下午2:25:36
	 * @param groups
	 * @param id
	 * @return
	 */
	public GroupPO queryGroupById(Collection<GroupPO> groups, Long id){
		if(groups == null) return null;
		for(GroupPO group : groups){
			if(group.getId().equals(id)){
				return group;
			}
		}
		return null;
	}
	
	public GroupMemberRolePermissionPO queryGroupMemberRolePermissionByGroupMemberId(Collection<GroupMemberRolePermissionPO> ps, Long memberId){
		if(ps == null) return null;
		for(GroupMemberRolePermissionPO p : ps){
			if(p.getGroupMemberId().equals(memberId)){
				return p;
			}
		}
		return null;
	}
	
	public TerminalBundleUserPermissionPO queryTerminalBundleUserPermissionByTerminalBundleId(Collection<TerminalBundleUserPermissionPO> ps, Long terminalBundleId){
		if(ps == null) return null;
		for(TerminalBundleUserPermissionPO p : ps){
			if(p.getTerminalBundleId().equals(terminalBundleId)){
				return p;
			}
		}
		return null;
	}
	
	public TerminalBundleConferenceHallPermissionPO queryTerminalBundleConferenceHallPermissionByTerminalBundleId(Collection<TerminalBundleConferenceHallPermissionPO> ps, Long terminalBundleId){
		if(ps == null) return null;
		for(TerminalBundleConferenceHallPermissionPO p : ps){
			if(p.getTerminalBundleId().equals(terminalBundleId)){
				return p;
			}
		}
		return null;
	}
	
	public List<SourceBO> querySourceBOsByMemberIds(Collection<SourceBO> sourceBOs, Long memberId){
		List<SourceBO> target = new ArrayList<SourceBO>();
		for(SourceBO sourceBO : sourceBOs){
			try{
				if(memberId.equals(sourceBO.getSrcVideoMemberId())){
					target.add(sourceBO);
				}
			}catch(Exception e){}			
		}
		return target;
	}
	
	public CombineVideoPO queryCombineVideoPOByCombineContentType(Collection<CombineVideoPO> combineVideos, CombineContentType combineContentType){
		for(CombineVideoPO combineVideo : combineVideos){
			if(combineContentType.equals(combineVideo.getCombineContentType())){
				return combineVideo;
			}
		}
		return null;
	}
	
	public CombineAudioPO queryCombineAudioPOByCombineContentType(Collection<CombineAudioPO> combineAudios, CombineContentType combineContentType){
		for(CombineAudioPO combineeAudio : combineAudios){
			if(combineContentType.equals(combineeAudio.getCombineContentType())){
				return combineeAudio;
			}
		}
		return null;
	}
	
	/**
	 *生成“指挥”/“会议”字符串<br/>
	 * <p>对于指挥</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午6:17:34
	 * @param type
	 * @return
	 */
	public String generateCommandString(BusinessType type){
		if(!BusinessType.COMMAND.equals(type)){
			return "会议";
		}else{
			//普通指挥和专向指挥
			return serverProps.getCommandString();
		}
	}
	
	/**
	 *生成“协同指挥”/“协同会议”/“发言”字符串<br/>
	 * <p>考虑是用“协同”还是用“协同指挥”</p>
	 * <p>当页面主动请求后台时，会按照指挥/会议区分协同/发言，不会混用接口；但是成员入会时，cooperate的成员就需要通过这里解析为“协同”/“发言”</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午6:17:34
	 * @param type
	 * @return
	 */
	public String generateCooperateString(BusinessType type){
		if(!BusinessType.COMMAND.equals(type)){
			return "发言";
		}else{
			//普通指挥和专向指挥
//			if(serverProps.getCommandString().equals("指挥")){
//				return "协同指挥";
//			}else{
//				return "协同会议";
//			}
			return "协同" + serverProps.getCommandString();
		}
	}
	
}
