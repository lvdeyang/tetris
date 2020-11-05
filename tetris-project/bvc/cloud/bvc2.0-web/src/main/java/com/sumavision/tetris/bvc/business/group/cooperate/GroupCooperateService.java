package com.sumavision.tetris.bvc.business.group.cooperate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: GroupCooperateService 
* @Description: 协同会议业务
* @author zsy
* @date 2019年10月24日 上午10:56:48 
*
 */
@Slf4j
@Service
public class GroupCooperateService {
	
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
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
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
	
	/**
	 * 重构，发起协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午9:08:02
	 * @param groupId
	 * @param memberIds 协同成员id列表
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Object start(Long groupId, List<Long> memberIds) throws Exception{
		
		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId==null || groupId.equals("")){
			log.info("开始协同指挥，会议id有误");
			return chairSplits;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
					
			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
			String commandString = commandCommonUtil.generateCommandString(groupType);
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
			}
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return chairSplits;
				}
			}
			
			//协同成员，校验是否已经被授权协同
			//查出这些成员，校验有没有未入会的，赋予协同角色
			
			//groupMember中查找
			List<GroupMemberPO> members = groupMemberDao.findByGroupIdAndIdIn(groupId,memberIds);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			if(chairmanMember != null){
				if(memberIds.contains(chairmanMember.getId())){
					throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同");
				}
			}
			
			RolePO cooperateRole = roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION);//TODO
			List<GroupMemberPO> cooperateMembers = new ArrayList<GroupMemberPO>();//统计本次新增的协同成员
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(cooperateRole.getId(), memberIds);
			for(GroupMemberPO member : members){
				if(memberIds.contains(member.getId())){
					//通过GroupMemberRolePermissionPO查询该成员是否已经是协同成员
					GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
					if(p == null){
						cooperateMembers.add(member);					
					}else{
						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 已经被授权协同");
					}
					
				}
			}
			
			//给这些人授予协同指挥角色。后续改成批量
//			List<Long> addRoleIds = new ArrayList<Long>();
//			addRoleIds.add(cooperateRole.getId());
			
			for(GroupMemberPO cooperateMember : cooperateMembers){
				agendaExecuteService.modifySoleMemberRole(groupId, cooperateMember.getId(), cooperateRole.getId(),false, false);
			}
			agendaExecuteService.executeToFinal(groupId);
			
	//		commandGroupDao.save(group);
			
			//发送websocket通知
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationAgree");
			message.put("businessId", group.getId().toString());
			List<String> names = businessCommonService.obtainMemberNames(cooperateMembers);
			message.put("businessInfo", group.getName() + " 主席授权 " + StringUtils.join(names.toArray(), ",") + " 协同" + commandString);
			message.put("splits", new JSONArray());
			List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
			businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
			
			//级联
			/*GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(type)){
					GroupBO groupBO = commandCascadeUtil.startCooperation(group, cooperateMembers);
					commandCascadeService.startCooperation(groupBO);
				}
			}*/
			
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
			
			operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，memberIds:" + memberIds.toString());
			return chairSplits;
		}
	}
	
	/**
	 * 重构，批量撤销授权协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:24:30
	 * @param groupId 组id
	 * @param userIds 用户id
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void revokeBatch(Long groupId, List<Long> userIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("停止协同指挥，会议id有误");
			return;
		}

		List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
		
			GroupPO group = groupDao.findOne(groupId);
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
				}else{
					return;
				}
			}
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
			}
		
			String commandString = commandCommonUtil.generateCommandString(groupType);
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			
			RolePO cooperateRole = roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION);
			List<GroupMemberPO> revokeMembers = new ArrayList<GroupMemberPO>();//统计本次解除的协同成员
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(cooperateRole.getId(), memberIds);
			for(GroupMemberPO member : members){
				if(memberIds.contains(member.getId())){
					//通过GroupMemberRolePermissionPO查询该成员是否已经是协同成员
					GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
					if(p != null){
						revokeMembers.add(member);					
					}else{
//						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 没有被授权协同");
					}
					
				}
			}
			
			//给这些人解绑协同指挥角色。后续改成批量
			List<Long> removeRoleIds = new ArrayList<Long>();
			removeRoleIds.add(cooperateRole.getId());
			
			Long commandAudienceId=roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_AUDIENCE).getId(); 
			for(GroupMemberPO revokeMember : revokeMembers){
				agendaExecuteService.modifySoleMemberRole(groupId, revokeMember.getId(),commandAudienceId, false,false);
			}
			agendaExecuteService.executeToFinal(groupId);
			
			//发送websocket通知
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationRevoke");
			message.put("businessId", group.getId().toString());
			List<String> names = businessCommonService.obtainMemberNames(revokeMembers);
			message.put("businessInfo", group.getName() + " 主席撤销 " + StringUtils.join(names.toArray(), ",") + " 协同" + commandString);
			message.put("splits", new JSONArray());
			List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
			businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
			
			/*
			//录制更新
			LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
			logic.merge(logicRecord);			
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, revokeMembersNames.toString());
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());	
			
			//级联
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(type)){
					GroupBO groupBO = commandCascadeUtil.stopCooperation(group, revokeMembers);
					commandCascadeService.stopCooperation(groupBO);
				}
			}
			*/
			
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
			
			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", memberIds:" + memberIds.toString());
		}
	}

//	/**
//	 * 
//	 * 发起协同会议<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月29日 上午11:36:03
//	 * @param groupId
//	 * @param memberIdArray 协同成员id列表
//	 * @return
//	 * @throws Exception
//	 */
//	public Object start0(Long groupId, List<Long> userIdArray) throws Exception{
//		UserVO user = userQuery.current();
//		JSONArray chairSplits = new JSONArray();
//		
//		if(groupId==null || groupId.equals("")){
//			log.info("开始协同指挥，会议id有误");
//			return chairSplits;
//		}
//		
//		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
//					
//		CommandGroupPO group = commandGroupDao.findOne(groupId);
//		GroupType groupType = group.getType();
//		String commandString = commandCommonUtil.generateCommandString(groupType);
//		if(groupType.equals(GroupType.MEETING)){
//			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
//		}
////		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//		
//		if(group.getStatus().equals(GroupStatus.STOP)){
//			if(!OriginType.OUTER.equals(group.getOriginType())){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
//			}else{
//				return chairSplits;
//			}
//		}
//		
//		if(userIdArray.contains(group.getUserId())){
//			if(groupType.equals(GroupType.BASIC)){
//				throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同" + commandString);
//			}else if(groupType.equals(GroupType.MEETING)){
//				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");//无用
//			}
//		}
//		
//		//协同成员，校验是否已经被授权协同
//		List<CommandGroupMemberPO> members = group.getMembers();
//		List<CommandGroupMemberPO> cooperateMembers = new ArrayList<CommandGroupMemberPO>();
//		for(CommandGroupMemberPO member : members){
//			if(userIdArray.contains(member.getUserId())){
//				if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
//					cooperateMembers.add(member);
//				}else{
//					if(groupType.equals(GroupType.BASIC)){
//						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同" + commandString);
//					}else if(groupType.equals(GroupType.MEETING)){
//						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经发言");//会议发言业务没有CONNECTING状态
//					}
//				}
//			}
//		}
//		
//		//校验协同成员是否入会
//		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
//			if(!cooperateMember.getMemberStatus().equals(MemberStatus.CONNECT)){
//				throw new BaseException(StatusCode.FORBIDDEN, cooperateMember.getUserName() + " 还未进入");
//			}
//		}
//		
//		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		List<CommandGroupForwardPO> forwards = group.getForwards();//new HashSet<CommandGroupForwardPO>();
//		
//		//除主席外，给每个成员建立转发
//		for(CommandGroupMemberPO member : members){
//			
//			if(member.isAdministrator()){
//				continue;
//			}
//			
//			for(CommandGroupMemberPO cooperateMember : cooperateMembers){
//				
//				//避免协同成员自己看自己
//				if(member.getUuid().equals(cooperateMember.getUuid())){
//					continue;
//				}
//				//查找这个member看这个cooperateMember是否已经存在
//				CommandGroupForwardPO existedForward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, cooperateMember.getId(), member.getId());
//				if(existedForward != null){
//					continue;
//				}
//				
//				CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
//						ForwardBusinessType.COOPERATE_COMMAND,
//						ExecuteStatus.UNDONE,
//						ForwardDstType.USER,
//						member.getId(),
//						cooperateMember.getId(),
//						cooperateMember.getSrcBundleId(),
//						cooperateMember.getSrcBundleName(),
//						cooperateMember.getSrcVenusBundleType(),
//						cooperateMember.getSrcLayerId(),
//						cooperateMember.getSrcVideoChannelId(),
//						"VenusVideoIn",//videoBaseType,
//						cooperateMember.getSrcBundleId(),
//						cooperateMember.getSrcBundleName(),
//						cooperateMember.getSrcBundleType(),
//						cooperateMember.getSrcLayerId(),
//						cooperateMember.getSrcAudioChannelId(),
//						"VenusAudioIn",//String audioBaseType,
//						null,//member.getDstBundleId(),
//						null,//member.getDstBundleName(),
//						null,//member.getDstBundleType(),
//						null,//member.getDstLayerId(),
//						null,//member.getDstVideoChannelId(),
//						"VenusVideoOut",//String dstVideoBaseType,
//						null,//member.getDstAudioChannelId(),
//						null,//member.getDstBundleName(),
//						null,//member.getDstBundleType(),
//						null,//member.getDstLayerId(),
//						null,//member.getDstAudioChannelId(),
//						"VenusAudioOut",//String dstAudioBaseType,
//						group.getUserId(),
//						group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
//						currentGear.getId(),//Long gearId,
//						DstDeviceType.WEBSITE_PLAYER,
//						null,//LiveType type,
//						null,//Long osdId,
//						null//String osdUsername);
//						);
//				c2m_forward.setGroup(group);
//				forwards.add(c2m_forward);		
//			}
//		}
//		
//		//每个协同成员看其他所有人
//		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
//			for(CommandGroupMemberPO member : members){
//				if(member.isAdministrator()){
//					continue;
//				}
//				//避免协同成员自己看自己
//				if(member.getUuid().equals(cooperateMember.getUuid())){
//					continue;
//				}
//				//查找这个cooperateMember看这个member是否已经存在
//				CommandGroupForwardPO existedForward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, member.getId(), cooperateMember.getId());
//				if(existedForward != null){
//					continue;
//				}
//				
//				CommandGroupForwardPO m2c_forward = new CommandGroupForwardPO(
//						ForwardBusinessType.COOPERATE_COMMAND,
//						ExecuteStatus.UNDONE,
//						ForwardDstType.USER,
//						cooperateMember.getId(),
//						member.getId(),
//						member.getSrcBundleId(),
//						member.getSrcBundleName(),
//						member.getSrcVenusBundleType(),
//						member.getSrcLayerId(),
//						member.getSrcVideoChannelId(),
//						"VenusVideoIn",//videoBaseType,
//						member.getSrcBundleId(),
//						member.getSrcBundleName(),
//						member.getSrcBundleType(),
//						member.getSrcLayerId(),
//						member.getSrcAudioChannelId(),
//						"VenusAudioIn",//String audioBaseType,
//						null,//cooperateMember.getDstBundleId(),
//						null,//cooperateMember.getDstBundleName(),
//						null,//cooperateMember.getDstBundleType(),
//						null,//cooperateMember.getDstLayerId(),
//						null,//cooperateMember.getDstVideoChannelId(),
//						"VenusVideoOut",//String dstVideoBaseType,
//						null,//cooperateMember.getDstAudioChannelId(),
//						null,//cooperateMember.getDstBundleName(),
//						null,//cooperateMember.getDstBundleType(),
//						null,//cooperateMember.getDstLayerId(),
//						null,//cooperateMember.getDstAudioChannelId(),
//						"VenusAudioOut",//String dstAudioBaseType,
//						group.getUserId(),
//						group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
//						currentGear.getId(),//Long gearId,
//						DstDeviceType.WEBSITE_PLAYER,
//						null,//LiveType type,
//						null,//Long osdId,
//						null//String osdUsername);
//						);
//				m2c_forward.setGroup(group);
//				forwards.add(m2c_forward);				
//			}
//		}
//		
//		commandGroupDao.save(group);
//		
//		//级联
//		GroupType type = group.getType();
//		if(!OriginType.OUTER.equals(group.getOriginType())){
//			if(GroupType.BASIC.equals(type)){
//				GroupBO groupBO = commandCascadeUtil.startCooperation(group, cooperateMembers);
//				commandCascadeService.startCooperation(groupBO);
//			}
//		}
//		
//		membersResponse(group, cooperateMembers, null);
//		
//		operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，userIds:" + userIdArray.toString());
//		return chairSplits;
//		}
//	}
//
//	/**
//	 * 撤销授权协同会议<br/>
//	 * <p>单个接口已弃用，使用批量revokeBatch</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月4日 下午1:26:41
//	 * @param userId
//	 * @param groupId
//	 * @return
//	 * @throws Exception
//	 */
//	public JSONArray revoke(Long userId, Long groupId) throws Exception{
//		UserVO user = userQuery.current();
//		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
//		
//			CommandGroupPO group = commandGroupDao.findOne(groupId);
//			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
//			
//			if(GroupStatus.STOP.equals(group.getStatus())){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
//			}
//			if(group.getType().equals(GroupType.MEETING)){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
//			}
//		
//			String commandString = commandCommonUtil.generateCommandString(group.getType());
//			JSONArray groupInfos = new JSONArray();
//			List<CommandGroupForwardPO> forwards = group.getForwards();
//			List<CommandGroupMemberPO> members = group.getMembers();
//			CommandGroupMemberPO revokeMember = commandCommonUtil.queryMemberByUserId(members, userId);
//			CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
//			List<Long> consumeIds = new ArrayList<Long>();
//			
//			if(revokeMember.getMessageCoId() != null){
////				websocketMessageService.consume(revokeMember.getMessageCoId());
//				consumeIds.add(revokeMember.getMessageCoId());
//				revokeMember.setMessageCoId(null);
//			}
//			
//			revokeMember.setCooperateStatus(MemberStatus.DISCONNECT);
//			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(revokeMember.getId()).getList();
//			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
//			
//			//给正在观看的成员发送的消息
//			JSONObject message = new JSONObject();
//			message.put("businessType", "cooperationRevoke");
//			//TODO: businessId中添加其他信息
//			message.put("businessId", group.getId().toString());
//			message.put("splits", new JSONArray());
//			if(group.getType().equals(GroupType.BASIC)){
//				message.put("businessInfo", group.getName() + " 主席撤销授权 " + revokeMember.getUserName() + " 协同" + commandString);
//			}else if(group.getType().equals(GroupType.MEETING)){
//				message.put("businessInfo", group.getName() + " 主席停止 " + revokeMember.getUserName() + " 发言");//无用
//			}
//			
//			//给被撤销成员发消息 TODO:信息可能不足
//			messageCaches.add(new MessageSendCacheBO(revokeMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName()));
////			WebsocketMessageVO ws = websocketMessageService.send(revokeMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
////			consumeIds.add(ws.getId());
//			
//			//释放其他成员的播放器，删除转发PO
//			List<CommandGroupUserPlayerPO> needClosePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			for(CommandGroupForwardPO forward : relativeForwards){
//				forward.setGroup(null);
//				//TODO:释放播放器没做好
////				if(forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
//					CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
//					JSONArray splits = new JSONArray();
//					message.put("splits", splits);
//					for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
//						if(player.getBundleId().equals(forward.getDstVideoBundleId())
//								&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
//							
//							//forward状态是DONE的，close播放器
////							if(forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
//								needClosePlayers.add(player);
////							}
//							
//							player.setFree();
//							needFreePlayers.add(player);
//							dstMember.getPlayers().remove(player);
//							
//							//给正在观看的成员填入分屏信息，发消息
//							JSONObject split = new JSONObject();
//							split.put("serial", player.getLocationIndex());
//							splits.add(split);
//						}
//					}
//					messageCaches.add(new MessageSendCacheBO(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName()));
////					WebsocketMessageVO ws2 = websocketMessageService.send(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
////					consumeIds.add(ws2.getId());
////				}
//			}
//			forwards.removeAll(relativeForwards);
//			commandGroupDao.save(group);
//			commandGroupUserPlayerDao.save(needFreePlayers);
//			
//			//关闭播放器，考虑怎么给不需要挂断的编码器计数
//			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
//			LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needClosePlayers, codec, chairman.getUserNum());
//			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needClosePlayers, codec, group.getUserId());
//			logic.merge(logicCastDevice);
//			executeBusiness.execute(logic, group.getName() + " 会议中撤销" + revokeMember.getUserName() + "的协同会议授权");
//			
//			for(MessageSendCacheBO cache : messageCaches){
//				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
//				consumeIds.add(ws.getId());
//			}
//			websocketMessageService.consumeAll(consumeIds);
//			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + "，userId:" + userId);
//			return groupInfos;		
//		}
//	}
//
//	/**
//	 * 批量撤销授权协同会议<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年2月18日 下午6:05:37
//	 * @param userIds
//	 * @param groupId
//	 * @return
//	 * @throws Exception
//	 */
//	public JSONArray revokeBatch0(List<Long> userIds, Long groupId) throws Exception{
//		UserVO user = userQuery.current();
//		
//		if(groupId==null || groupId.equals("")){
//			log.info("停止协同指挥，会议id有误");
//			return new JSONArray();
//		}
//		
//		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
//		
//			CommandGroupPO group = commandGroupDao.findOne(groupId);
//			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
//			
//			if(GroupStatus.STOP.equals(group.getStatus())){
//				if(!OriginType.OUTER.equals(group.getOriginType())){
//					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
//				}else{
//					return new JSONArray();
//				}
//			}
//			if(group.getType().equals(GroupType.MEETING)){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
//			}
//		
//			String commandString = commandCommonUtil.generateCommandString(group.getType());
//			JSONArray groupInfos = new JSONArray();
//			List<CommandGroupForwardPO> forwards = group.getForwards();
//			List<CommandGroupMemberPO> members = group.getMembers();
//			List<CommandGroupMemberPO> revokeMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
//			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
//			List<Long> consumeIds = new ArrayList<Long>();
//			List<Long> revokeMemberIds = new ArrayList<Long>();
//			
//			StringBufferWrapper revokeMembersNames = new StringBufferWrapper();			
//			for(CommandGroupMemberPO revokeMember : revokeMembers){
//				if(revokeMember.getMessageCoId() != null){
//					consumeIds.add(revokeMember.getMessageCoId());
//					revokeMember.setMessageCoId(null);
//				}
//				revokeMemberIds.add(revokeMember.getId());
//				revokeMember.setCooperateStatus(MemberStatus.DISCONNECT);//TODO:是否需要先判断一下
//				revokeMembersNames.append(revokeMember.getUserName()).append("，");
//			}
//			revokeMembersNames.append("被主席撤销协同" + commandString);	
//			
//			//以这些成员为源或目的的协同转发
//			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsByMemberIds(forwards, revokeMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
//			List<CommandGroupForwardPO> allNeedDelForwards = new ArrayList<CommandGroupForwardPO>();
//			
//			//释放这些退出或删除成员的播放器，同时如果是删人就给被删的成员发消息
//			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			
//			//除主席外，处理所有成员，包括被撤销的
//			for(CommandGroupMemberPO member : members){
//				if(member.isAdministrator()){// || revokeMemberIds.contains(member.getId())){
//					continue;
//				}
//				List<CommandGroupUserPlayerPO> thisMemberFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//				JSONArray splits = new JSONArray();
//				for(CommandGroupForwardPO forward : relativeForwards){
//					if(member.getId().equals(forward.getDstMemberId())){
//						//目的是该成员的，找播放器
//						for(CommandGroupUserPlayerPO player : member.getPlayers()){
//							//播放器对应转发目的，且 这是一条协同转发，且 该转发的源是一个被撤销协同的成员
//							if(player.getBundleId().equals(forward.getDstVideoBundleId())){
//									//&& forward.getForwardBusinessType().equals(ForwardBusinessType.COOPERATE_COMMAND)//这个条件上头查询已经有了
////									&& revokeMemberIds.contains(forward.getSrcMemberId())){
//								
//								//源和目的都不是协同，才释放
//								if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
//									CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
//									if(srcMember.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
//										player.setFree();
//										needFreePlayers.add(player);
//										thisMemberFreePlayers.add(player);
//										allNeedDelForwards.add(forward);
//										
//										JSONObject split = new JSONObject();
//										split.put("serial", player.getLocationIndex());
//										splits.add(split);
//									}
//								}
//							}
//						}
//					}
//				}
//				member.getPlayers().removeAll(thisMemberFreePlayers);
//				log.info(member.getUserName() + " 释放了播放器个数：" + thisMemberFreePlayers.size());
//				
//				JSONObject message = new JSONObject();
//				message.put("businessType", "cooperationRevoke");
//				message.put("businessId", group.getId().toString());
//				message.put("businessInfo", revokeMembersNames.toString());
//				message.put("splits", splits);
//				messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
//			}
//			
//			forwards.removeAll(allNeedDelForwards);
//			log.info(revokeMembersNames.toString() + "，删除转发个数：" + allNeedDelForwards.size());
//			
//			commandGroupDao.save(group);
//			commandGroupUserPlayerDao.save(needFreePlayers);
//			
//			GroupType type = group.getType();
//			if(!OriginType.OUTER.equals(group.getOriginType())){
//				if(GroupType.BASIC.equals(type)){
//					GroupBO groupBO = commandCascadeUtil.stopCooperation(group, revokeMembers);
//					commandCascadeService.stopCooperation(groupBO);
//				}
//			}
//			
//			//发协议（删转发协议不用发，通过挂断播放器来删）
//			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
//			LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needFreePlayers, codec, chairmanMember.getUserNum());
//			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needFreePlayers, codec, group.getUserId());
//			logic.merge(logicCastDevice);
//			
//			//录制更新
//			LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
//			logic.merge(logicRecord);
//			
//			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, revokeMembersNames.toString());
//			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());			
//	
//			//发消息
//			for(MessageSendCacheBO cache : messageCaches){
//				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
//				consumeIds.add(ws.getId());
//			}
//			websocketMessageService.consumeAll(consumeIds);			
//			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", userIds:" + userIds.toString());
//			return groupInfos;
//		}
//	}
//
//	/**
//	 * 给[入会成员]选择观看协同的播放器<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年4月3日 上午9:58:52
//	 * @param group
//	 * @param cooperateMembers
//	 * @throws Exception
//	 */
//	private void chosePlayersForMembers(CommandGroupPO group, List<CommandGroupMemberPO> cooperateMembers) throws Exception{
//		
//		if(cooperateMembers==null || cooperateMembers.size()==0){
//			return;
//		}
//		
//		List<CommandGroupMemberPO> members = group.getMembers();
//		List<CommandGroupForwardPO> forwards = group.getForwards();
//		Set<CommandGroupForwardPO> forwardsReadyToBeDone = commandCommonUtil.queryForwardsReadyToBeDone(members, forwards);
//		GroupType groupType = group.getType();
////		String commandString = commandCommonUtil.generateCommandString(groupType);
//		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//		
//		//除主席外，给每个[入会]成员统计需要的播放器个数，查找播放器，更新转发
//		for(CommandGroupMemberPO member : members){
//			
//			if(OriginType.OUTER.equals(member.getOriginType())){
//				continue;
//			}
//			
//			if(member.isAdministrator() || !MemberStatus.CONNECT.equals(member.getMemberStatus())){
//				continue;
//			}
//			
//			//以该member为目的，保证了源成员都已进会的，协同转发
//			List<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsByDstmemberIds(
//					forwardsReadyToBeDone, new ArrayListWrapper<Long>().add(member.getId()).getList(),
//					ForwardBusinessType.COOPERATE_COMMAND, ExecuteStatus.UNDONE);
//			
//			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needForwards.size(), false);
//			int usefulPlayersCount = players.size();
//			log.info(new StringBufferWrapper().append("需要的播放器数为 ").append(needForwards.size())
//					.append("， ").append(member.getUserName()).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
//			
//			for(CommandGroupForwardPO needForward : needForwards){
//				//如果有播放器，则设置dst
//				if(usefulPlayersCount > 0){
//					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
//					CommandGroupMemberPO cooperateMember = commandCommonUtil.queryMemberById(members, needForward.getSrcMemberId());					
//					player.setBusinessId(group.getId() + "-" + cooperateMember.getUserId());
//					if(groupType.equals(GroupType.BASIC)){
//						player.setBusinessName(group.getName() + "：" + cooperateMember.getUserName());// + "协同" + commandString);
//					}if(groupType.equals(GroupType.MEETING)){
//						player.setBusinessName(group.getName() + "：" + cooperateMember.getUserName());// + "发言");//无用
//					}
//					player.setPlayerBusinessType(PlayerBusinessType.COOPERATE_COMMAND);
//					
//					//给转发设置目的
//					needForward.setDstPlayer(player);
//					needForward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
//					player.setMember(member);
//					usefulPlayersCount--;
//				}else{
//					needForward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
//				}				
//			}
//			member.getPlayers().addAll(players);
//			needSavePlayers.addAll(players);
//		}
//			
//		commandGroupDao.save(group);
//		commandGroupUserPlayerDao.save(needSavePlayers);
//	}
//
//	/**
//	 * 
//	 * 批量处理协同成员的“接听”和“拒绝”<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月31日 上午10:01:36
//	 * @param group
//	 * @param acceptMembers
//	 * @param refuseMembers 废弃参数
//	 * @throws Exception
//	 */
//	private void membersResponse(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers, List<CommandGroupMemberPO> refuseMembers) throws Exception{
//					
//		GroupType groupType = group.getType();
//		String commandString = commandCommonUtil.generateCommandString(groupType);
//		if(null == acceptMembers) acceptMembers = new ArrayList<CommandGroupMemberPO>();
//		if(null == refuseMembers) refuseMembers = new ArrayList<CommandGroupMemberPO>();
//		
//		//处理同意用户，呼叫转发目标成员的播放器；给其他成员发消息
////		List<Long> acceptMemberIds = new ArrayList<Long>();//这个变量好像没用
//		List<String> acceptMemberNamesList = new ArrayList<String>();
////		List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();//不用了
////		Set<CommandGroupForwardPO> allNeedForwards = new HashSet<CommandGroupForwardPO>();
//		for(CommandGroupMemberPO acceptMember : acceptMembers){
//			acceptMember.setCooperateStatus(MemberStatus.CONNECT);
//			acceptMemberNamesList.add(acceptMember.getUserName());
////			acceptMemberIds.add(acceptMember.getId());
//		}
//		//save CONNECT状态
//		commandGroupDao.save(group);
//		
//		String acceptMemberNames = StringUtils.join(acceptMemberNamesList.toArray(), ",");
//		
//		chosePlayersForMembers(group, acceptMembers);
//		
//		List<CommandGroupMemberPO> members = group.getMembers();
//		List<CommandGroupForwardPO> forwards = group.getForwards();
//		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
//		List<Long> consumeIds = new ArrayList<Long>();
//		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
//		
//		//考虑如果停会之后执行，有没有问题
//		
//		//消息消费
//		try {
//			for(CommandGroupMemberPO acceptMember : acceptMembers){
//				if(acceptMember.getMessageCoId() != null){
//					consumeIds.add(acceptMember.getMessageCoId());
//					acceptMember.setMessageCoId(null);
//				}
//			}
//			for(CommandGroupMemberPO refuseMember : refuseMembers){
//				if(refuseMember.getMessageCoId() != null){
//					consumeIds.add(refuseMember.getMessageCoId());
//					refuseMember.setMessageCoId(null);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		//判断是否在进行
//		if(GroupStatus.STOP.equals(group.getStatus()) || GroupStatus.PAUSE.equals(group.getStatus())) {
//			return;
//		}
//		
//		//直接取出所有可执行的转发
//		Set<CommandGroupForwardPO> forwardsReadyAndCanBeDone = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, forwards);
//		
//		//给除主席外的其它进会成员，生成消息
//		for(CommandGroupMemberPO member : members){
//		
//			if(OriginType.OUTER.equals(member.getOriginType())){
//				continue;
//			}
//			
//			if(member.isAdministrator() || !MemberStatus.CONNECT.equals(member.getMemberStatus())){
//				continue;
//			}
//			
//			JSONObject message = new JSONObject();
//			message.put("businessType", "cooperationAgree");
//			message.put("businessId", group.getId().toString());
//			if(groupType.equals(GroupType.BASIC)){
//				message.put("businessInfo", group.getName() + " 主席授权 " + acceptMemberNames + " 协同" + commandString);
//			}else if(groupType.equals(GroupType.MEETING)){
//				message.put("businessInfo", group.getName() + " 成员 " + acceptMemberNames + " 发言");//无用
//			}
//			
//			//这里把所有的协同播放器全部返回，不管是不是本次业务的
//			List<BusinessPlayerVO> splits = new ArrayList<BusinessPlayerVO>();
//			for(CommandGroupUserPlayerPO player : member.getPlayers()){
//				if(PlayerBusinessType.COOPERATE_COMMAND.equals(player.getPlayerBusinessType())){
//					BusinessPlayerVO split = new BusinessPlayerVO().set(player);
//					splits.add(split);
//				}
//			}
//			message.put("splits", splits);
//			
//			messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
//		}		
//		
//		//本段应该没用了，暂时保留不动
//		//处理拒绝用户，释放其他成员的播放器，删除转发PO：查找该成员为源的转发，状态是UNDONE的，查找目的播放器；给主席发消息
//		List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//		for(CommandGroupMemberPO refuseMember : refuseMembers){
//			refuseMember.setCooperateStatus(MemberStatus.DISCONNECT);
//			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(refuseMember.getId()).getList();
//			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
//			
//			for(CommandGroupForwardPO forward : relativeForwards){
//				forward.setGroup(null);
//				if(forward.getExecuteStatus().equals(ExecuteStatus.UNDONE)
//						|| forward.getExecuteStatus().equals(ExecuteStatus.DONE)){//UNDONE表示找到了播放器，此时应该不会出现DONE
//					CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
//					if(OriginType.OUTER.equals(dstMember.getOriginType())){
//						continue;
//					}
//					for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
//						if(player.getBundleId().equals(forward.getDstVideoBundleId()) 
//								&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
//							player.setFree();
//							needFreePlayers.add(player);
//							dstMember.getPlayers().remove(player);
//						}
//					}
//				}
//			}
//			forwards.removeAll(relativeForwards);
//			
//			if(!OriginType.OUTER.equals(chairman.getOriginType())){
//				JSONObject message = new JSONObject();
//				message.put("businessType", "cooperationRefuse");
//				//考虑businessId中添加其他信息
//				message.put("businessId", group.getId().toString());
//				if(groupType.equals(GroupType.BASIC)){
//					message.put("businessInfo", refuseMember.getUserName() + " 拒绝与您协同" + commandString);
//				}else if(groupType.equals(GroupType.MEETING)){
//					message.put("businessInfo", refuseMember.getUserName() + " 拒绝发言");//无用
//				}
//				messageCaches.add(new MessageSendCacheBO(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName()));
//			}
//		}
//		
//		for(CommandGroupForwardPO forward : forwardsReadyAndCanBeDone){
//			forward.setExecuteStatus(ExecuteStatus.DONE);
//		}
//		
//		commandGroupDao.save(group);
////		commandGroupUserPlayerDao.save(needFreePlayers);
//		
//		//生成connectBundle，携带转发信息
//		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
//		LogicBO logic = commandBasicServiceImpl.openBundle(null, null, null, forwardsReadyAndCanBeDone, null, codec, chairman.getUserNum());
//		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, forwardsReadyAndCanBeDone, null, null, null, codec, group.getUserId());
//		logic.merge(logicCastDevice);		
//		
//		executeBusiness.execute(logic, group.getName() + " 协同会议成员接听和拒绝");
//		
//		//发消息
//		for(MessageSendCacheBO cache : messageCaches){
//			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
//			consumeIds.add(ws.getId());
//		}
//		websocketMessageService.consumeAll(consumeIds);
//			
//	}
}
