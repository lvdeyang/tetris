package com.sumavision.tetris.bvc.business.special;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.control.device.group.controller.GroupMemberRoleService;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandAuthorizePO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandReplacePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AudioType;
import com.sumavision.tetris.bvc.model.agenda.DestinationType;
import com.sumavision.tetris.bvc.model.agenda.LayoutPositionSelectionType;
import com.sumavision.tetris.bvc.model.agenda.LayoutSelectionType;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 特殊智慧业务的通用处理<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月2日 上午9:16:47
 */
@Slf4j
@Service
public class GroupSpecialCommonService {

	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private BusinessReturnService businessReturnService;

	@Autowired
	private PageInfoDAO pageInfoDao;

	@Autowired
	private PageTaskDAO pageTaskDao;

	@Autowired
	private CommonForwardDAO commonForwardDao;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;

	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private RoleExecuteService roleExecuteService;

	//@Autowired
	//private AgendaExecuteService agendaExecuteService;

	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;

	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private WebsocketMessageService websocketMessageService;

	@Autowired
	private BusinessCommonService businessCommonService;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	@Autowired
	private OperationLogService operationLogService;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private CommandCascadeUtil commandCascadeUtil;

	@Autowired
	private CommandCascadeService commandCascadeService;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
    private GroupMemberRoleService groupMemberRoleService;
	
	@Autowired
    private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
    private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
    private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
    private LayoutDAO layoutDao;
	
	/**
	 * 获取group中的特殊指挥（目前最多只有1个）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月5日 上午9:21:12
	 * @param group
	 * @return SECRET/COOPERATE/CROSS/REPLACE/AUTHORIZE/null
	 */
	public String querySpecialCommandString(GroupPO group){
		GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(group.getId());
		if(info == null) return null;
				
		if(info.isHasSecret()) return "SECRET";
		
		if(info.isHasReplace()) return "REPLACE";
		
		if(info.isHasAuthorize()) return "AUTHORIZE";
		
		if(info.isHasCooperate()) return "COOPERATE";
		
		if(info.isHasCross()) return "CROSS";
		
		return null;
	}
	
	/**
	 * 交换2个成员的角色<br/>
	 * <p>如果twoway为true，则两个成员交换角色；否则仅把fromMember的角色赋给toMember</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月2日 上午9:39:20
	 * @param fromMember 从该成员读取角色
	 * @param toMember 被修改角色的成员
	 * @param twoway true，则两个成员交换角色；false则仅把fromMember的角色赋给toMember
	 * @throws Exception
	 */
	public void exchangeRole(BusinessGroupMemberPO fromMember, BusinessGroupMemberPO toMember, boolean twoway){
		
		GroupMemberRolePermissionPO fromMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(fromMember.getId());
		GroupMemberRolePermissionPO toMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(toMember.getId());
		long fromMemberRoleId = fromMemberPer.getRoleId();
		long toMemberRoleId = toMemberPer.getRoleId();
		toMemberPer.setRoleId(fromMemberRoleId);
		toMember.setRoleId(fromMemberRoleId);
		groupMemberRolePermissionDao.save(toMemberPer);
		businessGroupMemberDao.save(toMember);
		
		if(twoway){
			fromMemberPer.setRoleId(toMemberRoleId);
			fromMember.setRoleId(toMemberRoleId);
			groupMemberRolePermissionDao.save(fromMemberPer);
			businessGroupMemberDao.save(fromMember);
		}
	}
	
	/**
	 * 校验是否有特殊指挥正在进行<br/>
	 * <p>专向、协同、越级、授权、接替</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月2日 上午10:56:08
	 * @param info
	 * @param doExcpetion true:如果有特殊指挥则抛错
	 * @return false表示没有特殊指挥
	 * @throws BaseException
	 */
	public boolean checkHasSpecialCommand(GroupCommandInfoPO info, boolean doExcpetion) throws BaseException{
		
		if(info == null) return false;
		
		if(info.isHasSecret()){
			if(doExcpetion) throw new BaseException(StatusCode.FORBIDDEN, "正在进行专向指挥");
			else return true;
		}
		
		if(info.isHasCooperate()){
			if(doExcpetion) throw new BaseException(StatusCode.FORBIDDEN, "正在进行协同指挥");
			else return true;
		}

		if(info.isHasCross()){
			if(doExcpetion) throw new BaseException(StatusCode.FORBIDDEN, "正在进行越级指挥");
			else return true;
		}

		if(info.isHasAuthorize()){
			if(doExcpetion) throw new BaseException(StatusCode.FORBIDDEN, "正在进行授权指挥");
			else return true;
		}

		if(info.isHasReplace()){
			if(doExcpetion) throw new BaseException(StatusCode.FORBIDDEN, "正在进行接替指挥");
			else return true;
		}
		
		return false;
	}
	
	/**
	 * 生成直接与group相关的议程转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月2日 下午5:06:59
	 * @param groupId 业务id
	 * @param memberForwards 议程转发的源和目的
	 * @throws Exception
	 */
	public List<AgendaForwardPO> createGroupForward(Long groupId, List<MemberForward> memberForwards) throws Exception{
		
		//取出一分屏
		List<LayoutPO> layoutList = layoutDao.findByPositionNum(1L);
		if(layoutList.size() < 1){
			throw new BaseException(StatusCode.FORBIDDEN, "没有配置一分屏虚拟源");
		}
		LayoutPO singleLayout = layoutList.get(0);
		
		List<AgendaForwardPO> agendaForwardList = new ArrayList<AgendaForwardPO>();
		List<AgendaForwardSourcePO> agendaForwardSourceList = new ArrayList<AgendaForwardSourcePO>();
		List<AgendaForwardDestinationPO> agendaForwardDestinationList = new ArrayList<AgendaForwardDestinationPO>();
		
		for(int i = 0; i < memberForwards.size(); i++){
			//创建议程转发。
			AgendaForwardPO agendaForward = new AgendaForwardPO();
			agendaForward.setLayoutId(singleLayout.getId());
			agendaForward.setLayoutSelectionType(LayoutSelectionType.CONFIRM);
			agendaForward.setAudioType(AudioType.CUSTOM);
			agendaForward.setVolume(50);
			agendaForward.setBusinessId(groupId);
			agendaForward.setAgendaForwardBusinessType(BusinessInfoType.COMMAND_FORWARD);
			agendaForwardList.add(agendaForward);
		}
		agendaForwardDao.save(agendaForwardList);
		
		int count = 0;
		Map<Long, StringBuffer> dstMemberIdAndMessage = new HashMap<Long, StringBuffer>();
		for(MemberForward memberForward : memberForwards){
				
			BusinessGroupMemberPO srcMember = memberForward.getSrcMember();
			BusinessGroupMemberPO dstMember = memberForward.getDstMember();
			
			AgendaForwardPO agendaForward = agendaForwardList.get(count++);
			agendaForward.setName(srcMember.getName() +" 转发给 ：" + dstMember.getName());
			String message = srcMember.getName() +"开始转发给:" + dstMember.getName()+"； ";
			if(dstMemberIdAndMessage.get(dstMember.getId()) == null){
				dstMemberIdAndMessage.put(dstMember.getId(), new StringBuffer(message));
			}else{
				dstMemberIdAndMessage.get(dstMember.getId()).append(message);
			}
			
			//创建议程转发源
			List<BusinessGroupMemberTerminalChannelPO> channels = srcMember.getChannels();
			Optional<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelOptional = channels.stream().filter(channl->{
				return TerminalChannelType.VIDEO_ENCODE.equals(channl.getTerminalChannelType());
			}).findFirst();
			
			AgendaForwardSourcePO source = new AgendaForwardSourcePO();
			source.setSourceType(SourceType.GROUP_MEMBER_CHANNEL);
			source.setLayoutPositionSelectionType(LayoutPositionSelectionType.AUTO_INCREMENT);
			source.setIsLoop(false);
			source.setSerialNum(1);//这是一分屏的position的serialNum
			source.setAgendaForwardId(agendaForward.getId());
			if(videoEncodeChannelOptional.isPresent()){
				Long sourceId = videoEncodeChannelOptional.get().getId();
				source.setSourceId(sourceId);
			}
			agendaForwardSourceList.add(source);
			
			//创建议程转发目的
			AgendaForwardDestinationPO destination = new AgendaForwardDestinationPO();
			destination.setDestinationId(dstMember.getId());
			destination.setDestinationType(DestinationType.GROUP_MEMBER);
			destination.setAgendaForwardId(agendaForward.getId());
			destination.setUpdateTime(new Date());
			agendaForwardDestinationList.add(destination);
		}
		
		//保存
		agendaForwardDao.save(agendaForwardList);
		agendaForwardSourceDao.save(agendaForwardSourceList);
		agendaForwardDestinationDao.save(agendaForwardDestinationList);
		
		return agendaForwardList;
	}
	
	/**
	 * 清理直接与group相关的议程转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 上午10:27:16
	 * @param groupId 会议组id
	 * @param forwardIds 议程转发id
	 * @return
	 */
	public void clearGroupForward(List<AgendaForwardPO> agendaforawrds){
		
		//清理议程转发、议程转发源、议程转发目的
		List<Long> forwardIds = agendaforawrds.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardIdIn(forwardIds);
		List<AgendaForwardDestinationPO> destinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
		
		agendaForwardSourceDao.deleteInBatch(sources);
		agendaForwardDestinationDao.deleteInBatch(destinations);
		agendaForwardDao.deleteByIdIn(forwardIds);
	}
	
	/**
	 * 停会时使用：清除恢复各种特殊数据，把修改的角色恢复<br/>
	 * <p>不清除议程转发，由GroupService.stopBusinessForwardByChairman()进行清除</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 下午5:05:47
	 * @param group
	 */
	public void clearAllSpecialCommand(GroupPO group){
		GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(group.getId());
		if(info == null) return;
		List<BusinessGroupMemberPO> members = group.getMembers();
		
		//清除接替指挥
		if(info.isHasReplace()){
			List<GroupCommandReplacePO> replaces = info.getReplaces();
			for(GroupCommandReplacePO replace : replaces){
				
				Long opMemberId = replace.getOpMemberId();
//				Long targetMemberId = replace.getTargetMemberId();

				BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryMemberById(members, opMemberId);
//				BusinessGroupMemberPO targetMember = tetrisBvcQueryUtil.queryMemberById(members, targetMemberId);
								
				//交换角色
//				exchangeRole(opMember, targetMember, true);
				
				//恢复角色opMemberPer
				GroupMemberRolePermissionPO opMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(opMember.getId());
				opMemberPer.setRoleId(replace.getOpMemberOriginRoleId());
				opMember.setRoleId(replace.getOpMemberOriginRoleId());
				groupMemberRolePermissionDao.save(opMemberPer);
				businessGroupMemberDao.save(opMember);
				
				//恢复角色targetMember（暂时不用）
				/*GroupMemberRolePermissionPO targetMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(targetMember.getId());
				targetMemberPer.setRoleId(replace.getTargetMemberOriginRoleId());
				targetMember.setRoleId(replace.getTargetMemberOriginRoleId());
				groupMemberRolePermissionDao.save(targetMemberPer);
				businessGroupMemberDao.save(targetMember);*/
			}
		}
		
		//清除授权指挥
		if(info.isHasAuthorize()){
			List<GroupCommandAuthorizePO> authorizes = info.getAuthorizes();
			for(GroupCommandAuthorizePO authorize : authorizes){
				
				Long acceptMemberId = authorize.getAcceptMemberId();
//				Long cmdMemberId = authorize.getCmdMemberId();				

				BusinessGroupMemberPO acceptMember = tetrisBvcQueryUtil.queryMemberById(members, acceptMemberId);
								
				//恢复角色
				GroupMemberRolePermissionPO acceptMemberPer = groupMemberRolePermissionDao.findByGroupMemberId(acceptMember.getId());
				acceptMemberPer.setRoleId(authorize.getAcceptMemberOriginRoleId());
				acceptMember.setRoleId(authorize.getAcceptMemberOriginRoleId());
				groupMemberRolePermissionDao.save(acceptMemberPer);
				businessGroupMemberDao.save(acceptMember);
			}
		}
		
		info.clearAll();
		groupCommandInfoDao.save(info);
		
	}
	
	public static class MemberForward{
		
		private BusinessGroupMemberPO srcMember;
		
		private BusinessGroupMemberPO dstMember;

		public BusinessGroupMemberPO getSrcMember() {
			return srcMember;
		}

		public MemberForward setSrcMember(BusinessGroupMemberPO srcMember) {
			this.srcMember = srcMember;
			return this;
		}

		public BusinessGroupMemberPO getDstMember() {
			return dstMember;
		}

		public MemberForward setDstMember(BusinessGroupMemberPO dstMember) {
			this.dstMember = dstMember;
			return this;
		}
		
	}

}
