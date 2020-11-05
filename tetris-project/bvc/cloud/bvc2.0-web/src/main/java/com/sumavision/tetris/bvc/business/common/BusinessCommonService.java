package com.sumavision.tetris.bvc.business.common;

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

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 业务快捷查询类<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月10日 下午1:19:13
 */
@Slf4j
@Service
public class BusinessCommonService {

	@Autowired
	private RoleDAO roleDao;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private PageTaskDAO pageTaskDao;

	@Autowired
	private GroupMemberDAO groupMemberDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;

	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;

	@Autowired
	private ResourceBundleDAO resourceBundleDao;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private CommonForwardDAO commonForwardDao;

	@Autowired
	private ResourceChannelDAO resourceChannelDao;

	@Autowired
	private FolderUserMapDAO folderUserMapDao;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private PageTaskService pageTaskService;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;

	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;

	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;

	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;

	@Autowired
	private CommandVodService commandVodService;

	@Autowired
	private ResourceServiceClient resourceServiceClient;

	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private WebsocketMessageService websocketMessageService;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private CommonQueryUtil commonQueryUtil;

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
	private ConferenceCascadeService conferenceCascadeService;
	
	@Autowired
	private BusinessReturnService businessReturnService;

	public RolePO queryGroupMemberRole(GroupPO group) {
		BusinessType businessType = group.getBusinessType();
		if (BusinessType.COMMAND.equals(businessType)) {
			return roleDao.findByInternalRoleType(InternalRoleType.COMMAND_AUDIENCE);
		} else if (BusinessType.MEETING_QT.equals(businessType)) {
			return roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		}
		return null;
	}

	public RolePO queryGroupChairmanRole(GroupPO group) {
		BusinessType businessType = group.getBusinessType();
		if (BusinessType.COMMAND.equals(businessType)) {
			return roleDao.findByInternalRoleType(InternalRoleType.COMMAND_CHAIRMAN);
		} else if (BusinessType.MEETING_QT.equals(businessType)) {
			return roleDao.findByInternalRoleType(InternalRoleType.MEETING_CHAIRMAN);
		}
		return null;
	}

	public List<Long> fromUserIdsToMemberIds(Long groupId, List<Long> userIds) {
		List<String> originIds = new ArrayList<String>();
		for (Long userId : userIds) {
			originIds.add(userId.toString());
		}
		List<Long> memberIds = groupMemberDao.findIdsByGroupIdAndOriginIds(groupId, originIds);
		return memberIds;
	}

	/** 只能查出用户成员，设备成员会被忽略 */
	public List<UserBO> queryUsersByGroupMembers(Collection<GroupMemberPO> members) {
		List<String> userIdList = new ArrayList<String>();
		for (GroupMemberPO member : members) {
			userIdList.add(member.getOriginId());
		}
		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
		List<UserBO> commandUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		return commandUserBos;
	}

	public Set<Long> obtainTerminalBundleIdsFromTerminalChannel(List<TerminalChannelPO> channels) {
		Set<Long> ids = new HashSet<Long>();
		for (TerminalChannelPO channel : channels) {
			ids.add(channel.getTerminalBundleId());
		}
		return ids;
	}

	public Set<String> obtainChannelIdsFromTerminalChannel(List<TerminalChannelPO> channels) {
		Set<String> ids = new HashSet<String>();
		for (TerminalChannelPO channel : channels) {
			ids.add(channel.getRealChannelId());
		}
		return ids;
	}

	public Set<Long> obtainTerminalBundleIdsFromTerminalBundleChannel(List<TerminalBundleChannelPO> channels) {
		Set<Long> ids = new HashSet<Long>();
		for (TerminalBundleChannelPO channel : channels) {
			ids.add(channel.getTerminalBundleId());
		}
		return ids;
	}

	public Set<String> obtainChannelIdsFromTerminalBundleChannel(List<TerminalBundleChannelPO> channels) {
		Set<String> ids = new HashSet<String>();
		for (TerminalBundleChannelPO channel : channels) {
			ids.add(channel.getChannelId());
		}
		return ids;
	}

	/*获取成员名字*/
	public List<String> obtainMemberNames(List<GroupMemberPO> members) {
		List<String> memberNames = new ArrayList<String>();
		for (GroupMemberPO member : members) {
			memberNames.add(member.getName());
		}
		return memberNames;
	}

	public List<Long> obtainMemberIds(Long groupId, boolean connected, boolean includingChairman) {
		List<Long> memberIds = new ArrayList<Long>();
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		for (GroupMemberPO member : members) {
			boolean fit = true;
			if (connected) {
				if (!GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())) {
					fit = false;
				}
			}
			if (!includingChairman) {
				if (member.getIsAdministrator()) {
					fit = false;
				}
			}
			if (fit) {
				memberIds.add(member.getId());
			}
		}
		return memberIds;
	}

	public List<Long> obtainGroupMemberRolePermissionPOIds(List<GroupMemberRolePermissionPO> ps) {
		List<Long> ids = new ArrayList<Long>();
		for (GroupMemberRolePermissionPO p : ps) {
			ids.add(p.getRoleId());
		}
		return ids;
	}

	public List<Long> obtainMemberIds(List<GroupMemberPO> members) {
		List<Long> memberIds = new ArrayList<Long>();
		for (GroupMemberPO member : members) {
			memberIds.add(member.getId());
		}
		return memberIds;
	}

	public void notifyUserInfo(List<Long> userIds, String message, WebsocketMessageType type) throws Exception {
		List<Long> consumeIds = new ArrayList<Long>();
		for (Long userId : userIds) {
			// 这里缺少判断该userID是否是本系统的
			WebsocketMessageVO ws = websocketMessageService.send(userId, message, type);
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}

	public void notifyMemberInfo(List<Long> memberIds, String message, WebsocketMessageType type) throws Exception {

		List<GroupMemberPO> members = groupMemberDao.findAll(memberIds);
		List<Long> userIds = new ArrayList<Long>();
		for (GroupMemberPO member : members) {
			if (GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())
					&& !OriginType.OUTER.equals(member.getOriginType())) {
				userIds.add(Long.parseLong(member.getOriginId()));
			}
		}

		List<Long> consumeIds = new ArrayList<Long>();
		if(businessReturnService.getSegmentedExecute()){
			for (Long userId : userIds) {
				MessageSendCacheBO cache = new MessageSendCacheBO(userId, message, type);
				businessReturnService.add(null, cache, null);
			}
		}else{
			for (Long userId : userIds) {
				WebsocketMessageVO ws = websocketMessageService.send(userId, message, type);
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}

	/**
	 * 比较2个成员的层级高低，主席的高，没有主席则平级 
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午1:28:09
	 * 
	 * @param member1
	 * @param member2
	 * @return
	 */
	public int compareLevelByMemberIsChairman(GroupMemberPO member1, GroupMemberPO member2) {
		if (member1.getIsAdministrator())
			return 1;
		else if (member2.getIsAdministrator())
			return -1;
		else
			return 0;
	}

	/**
	 * 执行一个会议中所有可以执行的转发<br/>
	 * <p>
	 * 线程不安全，调用处必须使用 command-group-{groupId} 加锁
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午1:18:44
	 * 
	 * @param group
	 *            注意传入前应先把group、members、forwards的状态save正确
	 * @param doPersistence
	 *            是否持久化转发的执行状态为UNDONE，通常使用true
	 * @param doProtocol
	 *            是否下发协议
	 * @return
	 * @throws Exception
	 */
	public LogicBO startGroupForwards(GroupPO group, boolean doPersistence, boolean doProtocol) throws Exception {
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(group.getId());
		// GroupMemberPO chairmanMember =
		// commandCommonUtil.queryChairmanMember(members);
		List<CommonForwardPO> forwards = commonForwardDao.findByBusinessId(group.getId().toString());
		Set<CommonForwardPO> needForwards = queryForwardsReadyAndCanBeDone(group,members, forwards);
		for (CommonForwardPO needForward : needForwards) {
			needForward.setVideoStatus(ExecuteStatus.DONE);
			needForward.setAudioStatus(ExecuteStatus.DONE);
		}

		List<PageTaskPO> changeTasks = new ArrayList<PageTaskPO>();
		List<String> forwardUuids = new ArrayList<String>();
		for (CommonForwardPO forward : needForwards) {
			forwardUuids.add(forward.getUuid());
		}
		List<PageTaskPO> tasks = pageTaskDao.findByForwardUuidIn(forwardUuids);

		for (PageTaskPO task : tasks) {
			boolean change = false;
			if (ExecuteStatus.UNDONE.equals(task.getAudioStatus())) {
				task.setAudioStatus(ExecuteStatus.DONE);
				change = true;
			}
			if (ExecuteStatus.UNDONE.equals(task.getVideoStatus())) {
				task.setVideoStatus(ExecuteStatus.DONE);
				change = true;
			}
			if (change) {
				changeTasks.add(task);
			}
		}

		// 持久化
		if (doPersistence)
			groupDao.save(group);
		pageTaskDao.save(tasks);
		commonForwardDao.save(needForwards);

		// 生成forwardSet的logic
		// CommandGroupAvtplGearsPO currentGear =
		// commandCommonUtil.queryCurrentGear(group);
		// CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(),
		// currentGear);
		// LogicBO logic = openBundle(null, null, null, needForwards, null,
		// codec, chairmanMember.getUserNum());
		// LogicBO logicCastDevice =
		// commandCastServiceImpl.openBundleCastDevice(null, null, needForwards,
		// null, null, null, codec, group.getUserId());
		// logic.merge(logicCastDevice);
		//
		// //录制更新
		// LogicBO logicRecord =
		// commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
		// logic.merge(logicRecord);
		//
		// if(doProtocol){
		// ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, "执行
		// " + group.getName() + " 会议中的转发，共" + needForwards.size() + "个");
		// commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());
		// }
		return pageTaskService.reExecute(changeTasks, doProtocol);
	}
	
	/**
	 * 具备执行条件，且没有互斥业务的转发：<br/>
	 * 执行状态为UNDONE，源和目的成员都已经CONNECT，<br/>
	 * 该转发没有因为指挥暂停、专向指挥、静默操作而停止，才可以被执行。<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午10:49:19
	 * 
	 * @param members
	 *            必须覆盖forwards转发所涉及到的所有成员；建议从forward对应的group中get得到，避免脏读
	 * @param forwards
	 *            转发列表
	 * @return
	 */
	public Set<CommonForwardPO> queryForwardsReadyAndCanBeDone(GroupPO group,Collection<GroupMemberPO> members,
			Collection<CommonForwardPO> forwards) {
		Set<CommonForwardPO> needForwards = new HashSet<CommonForwardPO>();
		Set<CommonForwardPO> readyForwards = queryForwardsReadyToBeDone(members, forwards);
		for (CommonForwardPO forward : readyForwards) {
			if (whetherCanBeDone(group,forward)) {
				needForwards.add(forward);
			}
		}
		return needForwards;
	}




	/**
	 * @Title: 具备执行条件的转发：执行状态为UNDONE，源和目的成员都已经CONNECT<br/>
	 * @description 搭配 CommandCommonServiceImpl 的 whetherCanBeDone 方法一起使用
	 * @param members
	 *            必须覆盖forwards转发所涉及到的所有成员；建议从forward对应的group中get得到，避免脏读
	 * @param forwards
	 *            转发列表
	 * @throws Exception
	 * @return Set<CommandGroupForwardPO> needForwards 具备执行条件的转发列表
	 */
	public Set<CommonForwardPO> queryForwardsReadyToBeDone(Collection<GroupMemberPO> members,
			Collection<CommonForwardPO> forwards) {
		Set<CommonForwardPO> needForwards = new HashSet<CommonForwardPO>();
		Map<Long, GroupMemberPO> map = membersSetToMap(members);
		for (CommonForwardPO forward : forwards) {
			Long srcMemberId = forward.getSrcMemberId();
			Long dstMemberId = forward.getDstMemberId();
			// 执行状态为UNDONE，源和目的成员都已经CONNECT
			if ((forward.getVideoStatus().equals(ExecuteStatus.UNDONE)||forward.getAudioStatus().equals(ExecuteStatus.UNDONE))
					&& map.get(srcMemberId).getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)
					&& map.get(dstMemberId).getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)) {
				// 如果是协同指挥转发（取消）
				// if(ForwardBusinessType.COOPERATE_COMMAND.equals(forward.getForwardBusinessType())){
				// if(map.get(srcMemberId).getCooperateStatus().equals(MemberStatus.CONNECT)){
				// needForwards.add(forward);
				// }
				// }else{
				needForwards.add(forward);
				// }
			}
		}
		return needForwards;
	}

	// 把Set<CommandGroupMemberPO>以id为key转为map，供queryForwardsNeedToBeDone调用
	private Map<Long, GroupMemberPO> membersSetToMap(Collection<GroupMemberPO> members) {
		Map<Long, GroupMemberPO> map = new HashMap<Long, GroupMemberPO>();
		if (members == null) {
			return map;
		}
		for (GroupMemberPO member : members) {
			map.put(member.getId(), member);
		}
		return map;
	}

	
	/**
	 * 一条转发能否被执行（或从暂停中恢复）<br/>
	 * <p>
	 * 该转发没有因为会议暂停、专向会议、静默操作而停止时，可以被执行。需要另行判断转发的执行状态ExecuteStatus
	 * </p>
	 * <p>
	 * 搭配 CommandCommonUtil 的 queryForwardsReadyToBeDone 方法一起使用
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午1:42:03
	 * 
	 * @param forward
	 * @return
	 */
	public boolean whetherCanBeDone(GroupPO group,CommonForwardPO forward) {
		if (whetherStopForCommandPause(group,forward)) {
			return false;
		} else if (whetherStopForSecret(group,forward)) {
			return false;
		} else if (whetherStopForSilence(forward)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一条转发是否因为会议的暂停而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:16:37
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForCommandPause(GroupPO group ,CommonForwardPO forward){		
		if(group.getStatus().equals(GroupStatus.PAUSE)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断一条转发是否因为专项会议业务而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午2:45:15
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForSecret(GroupPO group,CommonForwardPO forward){
		//TODO 需要修改专项会议的业务
//		//以forward的源，作为源和目的去查找专向转发
//		
//		List<GroupMemberPO> members = groupMemberDao.findByGroupId(group.getId());
//		GroupMemberPO member = tetrisBvcQueryUtil.queryMemberById(members, forward.getSrcMemberId());
//		Long forwardSrcUserId = member.getId();
//		
//		//查找专向会议group，非STOP的，且成员都CONNECT的，判断成员是否是forward的源（后续如果业务有变动，需要改成根据forward来查询判断）
////		Set<CommandGroupForwardPO> secretForwards = new HashSet<CommandGroupForwardPO>();
//		List<GroupPO> secretGroups = groupDao.find(GroupType.SECRET);
//		for(GroupPO secretGroup : secretGroups){
//			//forward自己的会议不需要判断
//			if(secretGroup.getId().equals(group.getId())){
//				continue;
//			}
//			if(!secretGroup.getStatus().equals(GroupStatus.STOP)){
//				List<CommandGroupMemberPO> secretMembers = secretGroup.getMembers();
//				//先判断成员是否都CONNECT
//				for(CommandGroupMemberPO secretMember : secretMembers){
//					if(!secretMember.getMemberStatus().equals(MemberStatus.CONNECT)){
//						break;
//					}
//				}
//				//再判断成员是否是forward的源
//				for(CommandGroupMemberPO secretMember : secretMembers){
//					if(secretMember.getUserId().equals(forwardSrcUserId)){
//						return true;
//					}
//				}
////				for(CommandGroupForwardPO secretForward : secretGroup.getForwards()){
////					if(secretForward.getExecuteStatus().equals(ExecuteStatus.DONE)){
////						secretForwards.add(secretForward);
////					}
////				}
//			}
//		}
		return false;
	}
	
	/**
	 * 判断一条转发是否因为静默操作而暂停<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:45:06
	 * @param forward
	 * @return
	 */
	public boolean whetherStopForSilence(CommonForwardPO forward){
		GroupMemberPO srcMember = groupMemberDao.findOne(forward.getSrcMemberId());		
		if(!srcMember.getSilenceToHigher() && !srcMember.getSilenceToLower()){
			return false;
		}
		
		//自己转给自己的，false
		if(srcMember.getId().equals(forward.getDstMemberId())){
			return false;
		}
		
		//源和目的的上下级关系
		GroupMemberPO dstMember = groupMemberDao.findOne(forward.getDstMemberId());
		int levelCompare = compareLevelByMemberIsChairman(srcMember, dstMember);
		
		if(srcMember.getSilenceToHigher() && levelCompare<0){
			return true;
		}
		if(srcMember.getSilenceToLower() && levelCompare>0){
			return true;
		}		
		return false;
	}

}
