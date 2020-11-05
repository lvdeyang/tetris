package com.sumavision.tetris.bvc.business.group.speak;

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
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: GroupCooperateService 
* @Description: 会议发言、讨论业务
* @author zsy
* @date 2019年10月24日 上午10:56:48 
*
 */
@Slf4j
@Service
public class GroupSpeakService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
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
	private AgendaExecuteService agendaExecuteService;
	
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
	private BusinessReturnService businessReturnService;

	@Transactional(rollbackFor = Exception.class)
	public void speakAppointU(Long groupId, List<Long> userIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
			speakAppoint(groupId, memberIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void speakAppointM(Long groupId, List<Long> memberIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			speakAppoint(groupId, memberIds);
		}
	}
	
	/**
	 * 重构，指定发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午9:08:02
	 * @param groupId
	 * @param memberIds 发言成员id列表
	 * @return
	 * @throws Exception
	 */
	private Object speakAppoint(Long groupId, List<Long> memberIds) throws Exception{
		
		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		if(groupId==null || groupId.equals("")){
			log.info("指定发言，会议id有误");
			return chairSplits;
		}
							
		GroupPO group = groupDao.findOne(groupId);
		BusinessType groupType = group.getBusinessType();
		String commandString = tetrisBvcQueryUtil.generateCommandString(groupType);
//			if(!groupType.equals(GroupBusinessType.COMMAND)){
//				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
//			}
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			if(!OriginType.OUTER.equals(group.getOriginType())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}else{
				return chairSplits;
			}
		}
		
		if(isGroupUnderDiscussion(groupId)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
		}
		
		//发言人，校验是否已经被授权协同
		//查出这些成员，校验有没有未入会的，赋予发言人角色
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		if(chairmanMember != null){
			if(memberIds.contains(chairmanMember.getId())){
				throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行发言");
			}
		}
		
		//只能有1个发言人
		List<GroupMemberPO> speakingMembers = querySpeakMembers(groupId);
		if(speakingMembers.size() > 0){
			throw new BaseException(StatusCode.FORBIDDEN, speakingMembers.get(0).getName() + "正在发言");
		}

		RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
		List<GroupMemberPO> speakMembers = new ArrayList<GroupMemberPO>();//统计本次新增的发言人
		List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(speakRole.getId(), memberIds);
		for(GroupMemberPO member : members){
			if(memberIds.contains(member.getId())){
				//通过GroupMemberRolePermissionPO查询该成员是否已经是发言人（上边已经校验过）
//				GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
//				if(p == null){
				speakMembers.add(member);					
//				}else{
//					throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 已经在发言");
//				}
				
			}
		}
		
		//给这些人授予发言人角色。后续改成批量
		for(GroupMemberPO cooperateMember : speakMembers){
			agendaExecuteService.modifySoleMemberRole(groupId, cooperateMember.getId(), speakRole.getId(), false,false);
		}
		agendaExecuteService.executeToFinal(groupId);
		
//		commandGroupDao.save(group);
		
		//发送websocket通知
		JSONObject message = new JSONObject();
		message.put("businessType", "speakStart");
		message.put("businessId", group.getId().toString());
		List<String> names = businessCommonService.obtainMemberNames(speakMembers);
		message.put("businessInfo", group.getName() + " " + StringUtils.join(names.toArray(), ",") + " 开始发言");
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
		
		operationLogService.send(user.getNickname(), "指定发言", user.getNickname() + "指定发言，groupId:" + groupId + "，memberIds:" + memberIds.toString());
		return chairSplits;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void speakApply(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			if(group.getBusinessType().equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要申请发言");
			}
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			GroupMemberPO speakMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
			
			RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
//			List<GroupMemberPO> cooperateMembers = new ArrayList<GroupMemberPO>();//统计本次新增的协同成员
			GroupMemberRolePermissionPO ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberId(speakRole.getId(), speakMember.getId());
			
			if(ps != null){
				throw new BaseException(StatusCode.FORBIDDEN, "您正在发言");
			}
			
			if(isGroupUnderDiscussion(groupId)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			//只能有1个发言人
			List<GroupMemberPO> speakingMembers = querySpeakMembers(groupId);
			if(speakingMembers.size() > 0){
				throw new BaseException(StatusCode.FORBIDDEN, speakingMembers.get(0).getName() + "正在发言");
			}
			
//			List<CommandGroupMemberPO> speakMembers = commandCommonUtil.queryMembersByMemberStatusAndCooperateStatus(members, MemberStatus.CONNECT, MemberStatus.CONNECT);
//			if(speakMembers.size() > 0){
//				throw new BaseException(StatusCode.FORBIDDEN, speakMembers.get(0).getUserName() + "正在发言");
//			}
			
			//如果主席和申请人都不在该系统，则不需要处理（正常不会出现）
			if(OriginType.OUTER.equals(chairmanMember.getOriginType())
					&& OriginType.OUTER.equals(speakMember.getOriginType())){
				return;
			}
						
			if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
				//主席在该系统
				JSONObject message = new JSONObject();
				message.put("businessType", "speakApply");
				message.put("businessInfo", speakMember.getName() + "申请发言");
				message.put("businessId", group.getId().toString() + "-" + speakMember.getId());
				
				if(businessReturnService.getSegmentedExecute()){
					businessReturnService.add(null, new MessageSendCacheBO(Long.parseLong(chairmanMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND), group.getName() + "申请发言");
					businessReturnService.execute();
				}else{
					WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(chairmanMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND);
					websocketMessageService.consume(ws.getId());
					log.info(group.getName() + "申请发言");
				}
				
			}else{
				//级联：主席在外部系统（那么申请人在该系统）
//				GroupBO groupBO = commandCascadeUtil.speakerSetRequest(group, speakMember);
//				conferenceCascadeService.speakerSetRequest(groupBO);
			}
			
//			log.info(group.getName() + "申请发言");
		}
		operationLogService.send(user.getNickname(), "申请发言", user.getNickname() + "申请发言groupId:" + groupId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void speakApplyAgreeU(Long groupId, List<Long> userIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
			speakApplyAgree(groupId, memberIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void speakApplyAgreeM(Long groupId, List<Long> memberIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			speakApplyAgree(groupId, memberIds);
		}
	}
	
	private void speakApplyAgree(Long groupId, List<Long> memberIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("同意申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			BusinessType groupType = group.getBusinessType();
//			GroupSpeakType speakType = group.getSpeakType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			if(groupType.equals(BusinessType.COMMAND)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(isGroupUnderDiscussion(groupId)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			if(memberIds.size() > 1){
				throw new BaseException(StatusCode.FORBIDDEN, "只能同意1人进行发言");
			}
			
			//只能有1个发言人
			List<GroupMemberPO> speakingMembers = querySpeakMembers(groupId);
			if(speakingMembers.size() > 0){
				throw new BaseException(StatusCode.FORBIDDEN, speakingMembers.get(0).getName() + "正在发言");
			}
						
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			List<Long> consumeIds = new ArrayList<Long>();
			
			//发言人，校验是否已经在发言
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApplyAgree");
			message.put("businessInfo", group.getName() + "主席同意发言，您已开始发言");
			message.put("businessId", group.getId());
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			
			//这个判断正常情况没有用
			if(memberIds.contains(chairmanMember.getId())){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要发言");
			}
			
			List<GroupMemberPO> speakMembers = new ArrayList<GroupMemberPO>();//统计本次新增的发言人
			RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(speakRole.getId(), memberIds);
			for(GroupMemberPO member : members){
				
				if(memberIds.contains(member.getId())){
					
					//校验是否已经进会，防止此时有成员退出
					if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 已经退出");
					}
					
					//通过GroupMemberRolePermissionPO查询该成员是否已经是发言人
					GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
					if(p == null){
						speakMembers.add(member);
						
						//如果发言人在本系统，websocket通知
						if(!OriginType.OUTER.equals(member.getOriginType())){
							messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
						}
						
						//级联，如果操作人在本系统
						if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
							
//							//如果发言人在外部系统，级联通知发言人所在系统（该通知只发送，收到后不处理，通过“成员主动申请发言通知”来处理）
//							if(OriginType.OUTER.equals(member.getOriginType())){
//								GroupBO groupBO = commandCascadeUtil.speakerSetResponse(group, member, "1");
//								conferenceCascadeService.speakerSetResponse(groupBO);
//							}
//							
//							// “成员主动申请发言通知”给所有其它系统
//							GroupBO groupBO = commandCascadeUtil.speakerSet(group, member);
//							conferenceCascadeService.speakerSetProactiveNotice(groupBO);
						}
					}
				}
			}
			
//			speakStart(group, speakMembers, 0);
			//给这些人授予发言人角色。后续改成批量
			for(GroupMemberPO speakMember : speakMembers){
				agendaExecuteService.modifySoleMemberRole(groupId, speakMember.getId(), speakRole.getId(),false,false);
			}
			agendaExecuteService.executeToFinal(groupId);
			
			//发消息
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);
			}
			
		}
		operationLogService.send(user.getNickname(), "同意申请发言", user.getNickname() + "同意申请发言groupId:" + groupId + ",memberIds:" + memberIds.toString());
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void speakApplyDisagree(Long userId, Long groupId, List<Long> memberIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("拒绝申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP) || memberIds.size()==0){
				return;
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			GroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			List<GroupMemberPO> speakMembers = tetrisBvcQueryUtil.queryMembersByIds(members, memberIds);
			
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApplyDisagree");
			message.put("businessInfo", "主席拒绝了您的发言申请");
			message.put("businessId", group.getId().toString());
			for(GroupMemberPO speakMember : speakMembers){
					
				//如果发言人在本系统，websocket通知
				if(!OriginType.OUTER.equals(speakMember.getOriginType())){
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(speakMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));								
				}
				
				//如果操作人在本系统
				if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
					
					//如果发言人在外部系统，级联通知发言人所在系统
					if(OriginType.OUTER.equals(speakMember.getOriginType())){
//						GroupBO groupBO = commandCascadeUtil.speakerSetResponse(group, speakMember, "0");
//						conferenceCascadeService.speakerSetResponse(groupBO);
					}
				}
			}
			
			if(businessReturnService.getSegmentedExecute()){
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, group.getName() + " 主席拒绝了 " + speakMembers.get(0).getName() + " 等人的发言申请");
				}
				businessReturnService.execute();
			}else{
				for(MessageSendCacheBO cache : messageCaches){
					WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
					consumeIds.add(ws.getId());
				}
				websocketMessageService.consumeAll(consumeIds);
				
				log.info(group.getName() + " 主席拒绝了 " + speakMembers.get(0).getName() + " 等人的发言申请");
			}
		}
		operationLogService.send(user.getNickname(), "拒绝申请发言", user.getNickname() + "拒绝申请发言groupId:" + groupId + ", memberIds" + memberIds.toString());
	}
	
	/**
	 * 成员停止自己发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月7日 下午1:33:05
	 * @param userId 成员、停止发言者的userId
	 * @param groupId
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakStopByMember(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("成员停止发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
						
			//发言人，校验是否已经在发言
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//			List<GroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			GroupMemberPO speakMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
			RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
			GroupMemberRolePermissionPO ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberId(speakRole.getId(), speakMember.getId());
			if(ps == null){
				return;
			}else{
				//解绑协同指挥角色
				RolePO audienceRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
				agendaExecuteService.modifySoleMemberRole(groupId, speakMember.getId(), audienceRole.getId(),false, false);
			}
			agendaExecuteService.executeToFinal(groupId);
			
			//websocket通知其它成员.
			JSONObject message = new JSONObject();
			message.put("businessType", "speakStop");
			message.put("businessId", group.getId().toString());
			message.put("businessInfo", group.getName() + " " + speakMember.getName() + " 停止发言");
			message.put("splits", new JSONArray());
			List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, true);
			notifyMemberIds.remove(speakMember.getId());
			businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
			
			//级联，这里的判断条件有所变化，发起人如果不是OUTER，就给其它系统发级联协议
			//级联协议里，成员能自己取消吗？
			if(!OriginType.OUTER.equals(speakMember.getOriginType())){
				//级联取消发言只有单个协议，没有批量
//				GroupBO groupBO = commandCascadeUtil.speakerSetCancel(speakMember.getUserNum(), group, speakMember);
//				conferenceCascadeService.speakerSetCancel(groupBO);
			}
			
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
		}
		operationLogService.send(user.getNickname(), "停止发言", user.getNickname() + "停止发言groupId:" + groupId);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void speakStopByChairmanU(Long groupId, List<Long> userIds) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			List<Long> memberIds = businessCommonService.fromUserIdsToMemberIds(groupId, userIds);
			speakStopByChairmanM(groupId, memberIds);
		}
	}
	
	/**
	 * 重构，停止发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:24:30
	 * @param groupId
	 * @param memberIds
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakStopByChairmanM(Long groupId, List<Long> memberIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("停止会议发言，会议id有误");
			return;
		}
		
		GroupPO group = groupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			if(!OriginType.OUTER.equals(group.getOriginType())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
			}else{
				return;
			}
		}
		BusinessType groupType = group.getBusinessType();
//			if(!groupType.equals(GroupBusinessType.COMMAND)){
//				throw new BaseException(StatusCode.FORBIDDEN, "只能在指挥中进行协同");
//			}
	
		String commandString = commandCommonUtil.generateCommandString(groupType);
		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
		
		RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
		GroupMemberPO revokeMember =null;//统计本次解除的发言人
		List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(speakRole.getId(), memberIds);
		
		for(GroupMemberPO member : members){
			if(memberIds.contains(member.getId())){
				//通过GroupMemberRolePermissionPO查询该成员是否已经是协同成员
				GroupMemberRolePermissionPO p = tetrisBvcQueryUtil.queryGroupMemberRolePermissionByGroupMemberId(ps, member.getId());
				if(p != null){
					revokeMember=member;					
				}else{
//						throw new BaseException(StatusCode.FORBIDDEN, member.getName() + " 没有被授权协同");
				}
				
			}
		}
		
//		//给这些人解绑协同指挥角色。后续改成批量
//		List<Long> removeRoleIds = new ArrayListWrapper<Long>().add(speakRole.getId()).getList();
//		for(GroupMemberPO revokeMember : revokeMembers){
//			agendaExecuteService.modifyMemberRole(groupId, revokeMember.getId(), null, removeRoleIds, false);
//		}
		RolePO audienceRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		if(revokeMember!=null){
			agendaExecuteService.modifySoleMemberRole(groupId, revokeMember.getId(),audienceRole.getId(),false, false);
		}
		agendaExecuteService.executeToFinal(groupId);
		
		//发送websocket通知
		JSONObject message = new JSONObject();
		message.put("businessType", "speakStop");
		message.put("businessId", group.getId().toString());
		List<String> names = businessCommonService.obtainMemberNames(new ArrayListWrapper().add(revokeMember).getList());
		message.put("businessInfo", group.getName() + " " + StringUtils.join(names.toArray(), ",") + " 停止发言");
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

	@Transactional(rollbackFor = Exception.class)
	public void discussStart(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("开始讨论，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(isGroupUnderDiscussion(groupId)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.MEETING_QT)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
			
			//解绑所有发言人角色
//			RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
//			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//			List<Long> memberIds = businessCommonService.obtainMemberIds(members);			
//			List<GroupMemberRolePermissionPO> ps = groupMemberRolePermissionDao.findByRoleIdAndGroupMemberIdIn(speakRole.getId(), memberIds);
//			groupMemberRolePermissionDao.deleteInBatch(ps);
			
			//将除主席之外的所有人都设置为观众
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			RolePO commandAudienceRole= roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
			for(GroupMemberPO member:members){
				if(member.getIsAdministrator()){
					continue;
				}else{
					agendaExecuteService.modifySoleMemberRole(groupId, member.getId(), commandAudienceRole.getId(), false, false);
				}
			}
			
			//执行讨论议程，停止会议议程
			AgendaPO discussAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.MEETING_DISCUSS);
			AgendaPO meetingAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_MEETING);
			agendaExecuteService.runAndStopAgenda(groupId,
					new ArrayListWrapper<Long>().add(discussAgenda.getId()).getList(),
					new ArrayListWrapper<Long>().add(meetingAgenda.getId()).getList());
			
			//发送websocket通知
			JSONObject message = new JSONObject();
			message.put("businessType", "speakStart");
			message.put("businessId", group.getId().toString());
			message.put("businessInfo", group.getName() + " 开始全员讨论");
			message.put("splits", new JSONArray());
			List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
			businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
//				GroupBO groupBO = commandCascadeUtil.discussStart(group);
//				conferenceCascadeService.discussStart(groupBO);
			}
			
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
		}
		operationLogService.send(user.getNickname(), "开启讨论模式", user.getNickname() + "开启讨论模式groupId:" + groupId);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void discussStop(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("停止讨论，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(!isGroupUnderDiscussion(groupId)){
				log.info(group.getName() + " 会议已经在主席模式，本次“停止讨论”操作不处理，操作直接返回");
				return;
			}
			
			if(group.getStatus().equals(GroupStatus.STOP)){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				return;
			}
			
			BusinessType groupType = group.getBusinessType();
			if(!groupType.equals(BusinessType.MEETING_QT)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
			
			//停止讨论议程，执行会议议程
			AgendaPO discussAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.MEETING_DISCUSS);
			AgendaPO meetingAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.BASIC_MEETING);
			agendaExecuteService.runAndStopAgenda(groupId,
					new ArrayListWrapper<Long>().add(meetingAgenda.getId()).getList(),
					new ArrayListWrapper<Long>().add(discussAgenda.getId()).getList());
			
			//发送websocket通知
			JSONObject message = new JSONObject();
			message.put("businessType", "speakStop");
			message.put("businessId", group.getId().toString());
			message.put("businessInfo", group.getName() + " 停止讨论");
			message.put("splits", new JSONArray());
			List<Long> notifyMemberIds = businessCommonService.obtainMemberIds(groupId, true, false);
			businessCommonService.notifyMemberInfo(notifyMemberIds, message.toJSONString(), WebsocketMessageType.COMMAND);
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
//				GroupBO groupBO = commandCascadeUtil.discussStop(group);
//				conferenceCascadeService.discussStop(groupBO);
			}		
			
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.execute();
			}
		}
		operationLogService.send(user.getNickname(), "停止讨论模式", user.getNickname() + "停止讨论模式groupId:" + groupId);
	}
	
	private boolean isGroupUnderDiscussion(Long groupId){
		AgendaPO discussAgenda = agendaDao.findByBusinessInfoType(BusinessInfoType.MEETING_DISCUSS);
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupIdAndAgendaId(groupId, discussAgenda.getId());
		if(runningAgenda == null) return false;
		return true;
	}
	
	private List<GroupMemberPO> querySpeakMembers(Long groupId){
		RolePO speakRole = roleDao.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
		List<GroupMemberPO> speakMembers = groupMemberDao.findByGroupIdAndRoleId(groupId, speakRole.getId());
		return speakMembers;
	}
}
