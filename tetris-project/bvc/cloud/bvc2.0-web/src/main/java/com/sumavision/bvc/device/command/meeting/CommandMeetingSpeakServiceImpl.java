package com.sumavision.bvc.device.command.meeting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDstType;
import com.sumavision.bvc.command.group.enumeration.GroupSpeakType;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandMeetingServiceImpl 
* @Description: 指挥系统中的会议业务。各个方法根据需要加事物，公用方法不加事物
* @author zsy
* @date 2020年1月3日 上午10:56:48 
*
 */
@Slf4j
@Service
public class CommandMeetingSpeakServiceImpl {
	
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
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;

	/**
	 * 指定发言<br/>
	 * <p>指定后立刻开始，不需要同意</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:33:21
	 * @param userId 操作人
	 * @param groupId
	 * @param userIdArray
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakAppoint(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("指定发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			GroupSpeakType speakType = group.getSpeakType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			if(!groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(GroupSpeakType.DISCUSS.equals(speakType)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			if(userIdArray.contains(group.getUserId())){
				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");
			}
			
			if(userIdArray.size() > 1){
				throw new BaseException(StatusCode.FORBIDDEN, "只能指定1人进行发言");
			}

			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers0 = commandCommonUtil.queryMembersByMemberStatusAndCooperateStatus(members, MemberStatus.CONNECT, MemberStatus.CONNECT);
			if(speakMembers0.size() > 0){
				throw new BaseException(StatusCode.FORBIDDEN, speakMembers0.get(0).getUserName() + "正在发言");
			}
			
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			List<Long> consumeIds = new ArrayList<Long>();
						
			//发言人，校验是否已经在发言
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(userIdArray.contains(member.getUserId())){
					if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
						member.setCooperateStatus(MemberStatus.CONNECT);
						speakMembers.add(member);
					}else{
						if(groupType.equals(GroupType.BASIC)){
							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同会议");
						}else if(groupType.equals(GroupType.MEETING)){
							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 正在发言");
						}
					}
				}
			}
			
			//校验发言人是否入会
			JSONObject message = new JSONObject();
			message.put("businessType", "speakAppoint");
			message.put("businessInfo", group.getName() + "主席指定发言，您已开始发言");
			message.put("businessId", group.getId());
			for(CommandGroupMemberPO speakMember : speakMembers){
				if(!speakMember.getMemberStatus().equals(MemberStatus.CONNECT)){
					throw new BaseException(StatusCode.FORBIDDEN, speakMember.getUserName() + " 还未进入");
				}
				messageCaches.add(new MessageSendCacheBO(speakMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
			
			//级联
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.MEETING.equals(type)){
					//级联指定发言只有单个协议，没有批量
					for(CommandGroupMemberPO speakMember : speakMembers){
						GroupBO groupBO = commandCascadeUtil.speakerSet(group, speakMember);
						conferenceCascadeService.speakerSetByChairman(groupBO);
					}
				}
			}
			
			//这里有持久化
			speakStart(group, speakMembers, 0);
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
		operationLogService.send(user.getNickname(), "指定发言", user.getNickname() + "指定发言groupId:" + groupId + ", userIdArray:" + userIdArray.toString());
	}
	
	/**
	 * 申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午12:02:08
	 * @param userId 申请人
	 * @param groupId
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakApply(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupSpeakType speakType = group.getSpeakType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			if(!group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要申请发言");
			}
			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			CommandGroupMemberPO speakMember = commandCommonUtil.queryMemberByUserId(members, userId);
			if(speakMember.getCooperateStatus().equals(MemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, "您正在发言");
			}
			
			if(GroupSpeakType.DISCUSS.equals(speakType)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			List<CommandGroupMemberPO> speakMembers = commandCommonUtil.queryMembersByMemberStatusAndCooperateStatus(members, MemberStatus.CONNECT, MemberStatus.CONNECT);
			if(speakMembers.size() > 0){
				throw new BaseException(StatusCode.FORBIDDEN, speakMembers.get(0).getUserName() + "正在发言");
			}
			
			//如果主席和申请人都不在该系统，则不需要处理（正常不会出现）
			if(OriginType.OUTER.equals(chairmanMember.getOriginType())
					&& OriginType.OUTER.equals(speakMember.getOriginType())){
				return;
			}
			
			//级联
			if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
				//主席在该系统
				JSONObject message = new JSONObject();
				message.put("businessType", "speakApply");
				message.put("businessInfo", speakMember.getUserName() + "申请发言");
				message.put("businessId", group.getId().toString() + "-" + speakMember.getUserId());
				
				WebsocketMessageVO ws = websocketMessageService.send(chairmanMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
			}else{
				//主席在外部系统（那么申请人在该系统）
				GroupBO groupBO = commandCascadeUtil.speakerSetRequest(group, speakMember);
				conferenceCascadeService.speakerSetRequest(groupBO);
			}
			
			log.info(group.getName() + "申请发言");
		}
		operationLogService.send(user.getNickname(), "申请发言", user.getNickname() + "申请发言groupId:" + groupId);
	}
	
	/**
	 * 主席同意成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:34:35
	 * @param userId 操作人（应该是主席）
	 * @param groupId
	 * @param userIdArray 被同意的用户
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakApplyAgree(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("同意申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupSpeakType speakType = group.getSpeakType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			
			if(!group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			//这个判断正常情况没有用
			if(userIdArray.contains(group.getUserId())){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要发言");
			}
			
			if(GroupSpeakType.DISCUSS.equals(speakType)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "当前正在进行讨论");
			}
			
			if(userIdArray.size() > 1){
				throw new BaseException(StatusCode.FORBIDDEN, "只能同意1人进行发言");
			}
			
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers0 = commandCommonUtil.queryMembersByMemberStatusAndCooperateStatus(members, MemberStatus.CONNECT, MemberStatus.CONNECT);
			if(speakMembers0.size() > 0){
				throw new BaseException(StatusCode.FORBIDDEN, speakMembers0.get(0).getUserName() + "已经在发言止");
			}
			
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			List<Long> consumeIds = new ArrayList<Long>();
			
			//发言人，校验是否已经在发言
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApplyAgree");
			message.put("businessInfo", group.getName() + "主席同意发言，您已开始发言");
			message.put("businessId", group.getId());
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(userIdArray.contains(member.getUserId())){
					
					//校验是否已经进会，防止此时有成员退出
					if(!member.getMemberStatus().equals(MemberStatus.CONNECT)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经退出");
					}
					
					if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
						member.setCooperateStatus(MemberStatus.CONNECT);
						speakMembers.add(member);
							
						//如果发言人在本系统，websocket通知
						if(!OriginType.OUTER.equals(member.getOriginType())){
							messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
						}
						
						//如果操作人在本系统
						if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
							
							//如果发言人在外部系统，级联通知发言人所在系统（该通知只发送，收到后不处理，通过“成员主动申请发言通知”来处理）
							if(OriginType.OUTER.equals(member.getOriginType())){
								GroupBO groupBO = commandCascadeUtil.speakerSetResponse(group, member, "1");
								conferenceCascadeService.speakerSetResponse(groupBO);
							}
							
							// “成员主动申请发言通知”给所有其它系统
							GroupBO groupBO = commandCascadeUtil.speakerSet(group, member);
							conferenceCascadeService.speakerSetProactiveNotice(groupBO);
						}
					}else{
//						if(groupType.equals(GroupType.BASIC)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同会议");
//						}else if(groupType.equals(GroupType.MEETING)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 正在发言");
//						}
					}
				}
			}
			
			speakStart(group, speakMembers, 0);
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
		operationLogService.send(user.getNickname(), "同意申请发言", user.getNickname() + "同意申请发言groupId:" + groupId + ",userIds:" + userIdArray.toString());
	}
	

	/**
	 * 主席拒绝成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:37:12
	 * @param userId 操作人（应该是主席）
	 * @param groupId
	 * @param userIds 被拒绝的用户
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakApplyDisagree(Long userId, Long groupId, List<Long> userIds) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("拒绝申请发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP) || userIds.size()==0){
				return;
			}
			if(!group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<CommandGroupMemberPO> speakMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApplyDisagree");
			message.put("businessInfo", "主席拒绝了您的发言申请");
			message.put("businessId", group.getId().toString());
			for(CommandGroupMemberPO speakMember : speakMembers){
					
				//如果发言人在本系统，websocket通知
				if(!OriginType.OUTER.equals(speakMember.getOriginType())){
					messageCaches.add(new MessageSendCacheBO(speakMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));								
				}
				
				//如果操作人在本系统
				if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
					
					//如果发言人在外部系统，级联通知发言人所在系统
					if(OriginType.OUTER.equals(speakMember.getOriginType())){
						GroupBO groupBO = commandCascadeUtil.speakerSetResponse(group, speakMember, "0");
						conferenceCascadeService.speakerSetResponse(groupBO);
					}
				}
			}
			
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			log.info(group.getName() + " 主席拒绝了 " + speakMembers.get(0).getUserName() + " 等人的发言申请");
		}
		operationLogService.send(user.getNickname(), "拒绝申请发言", user.getNickname() + "拒绝申请发言groupId:" + groupId + ", userIds" + userIds.toString());
	}
	
	/**
	 * 成员停止发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:38:33
	 * @param userId
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
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
//			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
			if(!group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
						
			//发言人，校验是否已经在发言
			List<CommandGroupMemberPO> members = group.getMembers();
//			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			CommandGroupMemberPO speakMember = commandCommonUtil.queryMemberByUserId(members, userId);
			if(speakMember.getCooperateStatus().equals(MemberStatus.CONNECT)){
				speakMember.setCooperateStatus(MemberStatus.DISCONNECT);
				speakMembers.add(speakMember);
			}else{
				return;
			}
			
			//级联，这里的判断条件有所变化，发起人如果不是OUTER，就给其它系统发级联协议
			//TODO:级联协议里，成员能自己取消吗？
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(speakMember.getOriginType())){
				if(GroupType.MEETING.equals(type)){
					//级联取消发言只有单个协议，没有批量
					GroupBO groupBO = commandCascadeUtil.speakerSetCancel(speakMember.getUserNum(), group, speakMember);
					conferenceCascadeService.speakerSetCancel(groupBO);
				}
			}
			
			//这里有持久化
			speakStop(group, speakMembers, 0);
		}
		operationLogService.send(user.getNickname(), "停止发言", user.getNickname() + "停止发言groupId:" + groupId);
	}
	
	/**
	 * 主席停止多个成员发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:41:10
	 * @param userId
	 * @param groupId
	 * @param userIdArray
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void speakStopByChairman(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("主席停止成员发言，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
			if(!groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			//正常情况不会出现
			if(userIdArray.contains(group.getUserId())){
//				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");
			}
						
			//发言人，校验是否已经在发言
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(userIdArray.contains(member.getUserId())){
					if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
						member.setCooperateStatus(MemberStatus.DISCONNECT);
						speakMembers.add(member);
					}else{
						//该成员没有发言，不处理
					}
				}
			}
			
			//级联
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(chairmanMember.getOriginType())){
				if(GroupType.MEETING.equals(type)){
					//级联取消发言只有单个协议，没有批量
					for(CommandGroupMemberPO speakMember : speakMembers){
						GroupBO groupBO = commandCascadeUtil.speakerSetCancel(chairmanMember.getUserNum(), group, speakMember);
						conferenceCascadeService.speakerSetCancel(groupBO);
					}
				}
			}
			
			//这里有持久化
			speakStop(group, speakMembers, 0);
		}
		operationLogService.send(user.getNickname(), "停止发言", user.getNickname() + "停止发言groupId:" + groupId + ", userIds" + userIdArray.toString());
	}
	
	/**
	 * 开始讨论<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:44:23
	 * @param userId 操作人
	 * @param groupId
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void discussStart(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("开始讨论，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(GroupSpeakType.DISCUSS.equals(group.getSpeakType())){
				log.info(group.getName() + " 会议已经在讨论模式，本次“开始讨论”操作不处理，操作直接返回");
				return;
			}
			group.setSpeakType(GroupSpeakType.DISCUSS);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(!OriginType.OUTER.equals(group.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				}else{
					return;
				}
			}
			if(!groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
						
			//发言人：入会的，且没在发言的都进行发言
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(member.isAdministrator()){
					//主席不能发言！
					continue;
				}
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)
						&& member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
					member.setCooperateStatus(MemberStatus.CONNECT);
					speakMembers.add(member);
				}
				//讨论则把成员的发言状态都置为DISCONNECT
				if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
					member.setCooperateStatus(MemberStatus.DISCONNECT);
				}
			}
			
			//级联
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.MEETING.equals(type)){
					GroupBO groupBO = commandCascadeUtil.discussStart(group);
					conferenceCascadeService.discussStart(groupBO);
				}
			}
			
			speakStart(group, speakMembers, 1);
		}
		operationLogService.send(user.getNickname(), "开启讨论模式", user.getNickname() + "开启讨论模式groupId:" + groupId);
	}
	
	/**
	 * 停止讨论<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:44:46
	 * @param userId 操作人
	 * @param groupId
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void discussStop(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		
		if(groupId==null || groupId.equals("")){
			log.info("停止讨论，会议id有误");
			return;
		}
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(!GroupSpeakType.DISCUSS.equals(group.getSpeakType())){
				log.info(group.getName() + " 会议已经在主席模式，本次“停止讨论”操作不处理，操作直接返回");
				return;
			}
			group.setSpeakType(GroupSpeakType.CHAIRMAN);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				return;
			}
			if(!groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
						
			//发言人：除主席外，所有进会的成员都是发言人
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(member.isAdministrator()) continue;
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
//				if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
//					member.setCooperateStatus(MemberStatus.DISCONNECT);
					speakMembers.add(member);
				}
			}
			
			//级联
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.MEETING.equals(type)){
					GroupBO groupBO = commandCascadeUtil.discussStop(group);
					conferenceCascadeService.discussStop(groupBO);
				}
			}
			
			speakStop(group, speakMembers, 1);
		}
		operationLogService.send(user.getNickname(), "停止讨论模式", user.getNickname() + "停止讨论模式groupId:" + groupId);
	}
	
	
	/**
	 * 开始发言<br/>
	 * <p>持久化、推送消息、呼叫收发流</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午6:01:15
	 * @param group
	 * @param speakMembers 发言人列表，必须是已经进会、且新增的发言人，不能已经在发言
	 * @param mode 目前仅用于显示信息：0发言，1全员讨论；2加入讨论，在新成员或者新进入成员使用
	 * @return
	 * @throws Exception
	 */
	public void speakStart(CommandGroupPO group, List<CommandGroupMemberPO> speakMembers, int mode) throws Exception{
		//需要呼叫的播放器
		List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		//需要下发的转发
		Set<CommandGroupForwardPO> allNeedForwards = new HashSet<CommandGroupForwardPO>();
					
		//需要存储的播放器
		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		//需要存储的转发
		Set<CommandGroupForwardPO> newForwards = new HashSet<CommandGroupForwardPO>();
		
		List<Long> consumeIds = new ArrayList<Long>();
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		
		GroupType groupType = group.getType();
		
		//协同成员，校验是否已经被授权协同
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//统计发言人名
		List<String> speakNameList = new ArrayList<String>();
		for(CommandGroupMemberPO speakMember : speakMembers){
			speakMember.setCooperateStatus(MemberStatus.CONNECT);
			speakNameList.add(speakMember.getUserName());
		}
		String businessInfo = null;
		if(mode == 0){			
			businessInfo = StringUtils.join(speakNameList.toArray(), "，") + " 开始发言";
		}else if(mode == 1){
			businessInfo = "开始全员讨论";
		}else if(mode == 2){			
			businessInfo = StringUtils.join(speakNameList.toArray(), "，") + " 加入讨论";
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
		
		//除主席外，给每个成员查找speakMembers.size()个播放器，建立转发
		for(CommandGroupMemberPO member : members){
			
			if(OriginType.OUTER.equals(member.getOriginType())){
				continue;//TODO:不给OUTER成员建立转发，不太好
			}
			
			if(member.isAdministrator()){
				continue;
			}
			
//			JSONArray splits = new JSONArray();			
			List<BusinessPlayerVO> splits = new ArrayList<BusinessPlayerVO>();
			String cooStr = "";
			int needPlayerCount = speakMembers.size();
			if(speakMembers.contains(member)){
				//这是协同成员
				needPlayerCount--;
				cooStr = " 也是发言人，";
			}
			
			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needPlayerCount, false);
			int usefulPlayersCount = players.size();
			log.info(new StringBufferWrapper().append("发言人数为 ").append(speakMembers.size())
					.append("， ").append(member.getUserName()).append(cooStr).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
			
			for(CommandGroupMemberPO speakMember : speakMembers){
				
				//避免协同成员自己看自己
				if(member.getUuid().equals(speakMember.getUuid())){
					continue;
				}
				
				CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
						ForwardBusinessType.COOPERATE_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						member.getId(),
						speakMember.getId(),
						speakMember.getSrcBundleId(),
						speakMember.getSrcBundleName(),
						speakMember.getSrcVenusBundleType(),
						speakMember.getSrcLayerId(),
						speakMember.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						speakMember.getSrcBundleId(),
						speakMember.getSrcBundleName(),
						speakMember.getSrcBundleType(),
						speakMember.getSrcLayerId(),
						speakMember.getSrcAudioChannelId(),
						"VenusAudioIn",//String audioBaseType,
						null,//member.getDstBundleId(),
						null,//member.getDstBundleName(),
						null,//member.getDstBundleType(),
						null,//member.getDstLayerId(),
						null,//member.getDstVideoChannelId(),
						"VenusVideoOut",//String dstVideoBaseType,
						null,//member.getDstAudioChannelId(),
						null,//member.getDstBundleName(),
						null,//member.getDstBundleType(),
						null,//member.getDstLayerId(),
						null,//member.getDstAudioChannelId(),
						"VenusAudioOut",//String dstAudioBaseType,
						group.getUserId(),
						group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
						currentGear.getId(),//Long gearId,
						DstDeviceType.WEBSITE_PLAYER,
						null,//LiveType type,
						null,//Long osdId,
						null//String osdUsername);
						);
				c2m_forward.setGroup(group);
				newForwards.add(c2m_forward);
				
				//如果有播放器，则设置dst
				if(usefulPlayersCount > 0){
					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
					
					player.setBusinessId(group.getId().toString() + "-" + speakMember.getUserId());//如果需要改成c2m_forward.getId()，那么需要先save获得id
					if(groupType.equals(GroupType.BASIC)){
						player.setBusinessName(group.getName() + "-" + speakMember.getUserName() + "协同会议");
					}if(groupType.equals(GroupType.MEETING)){
						player.setBusinessName(group.getName() + "-" + speakMember.getUserName() + "发言");
					}
					player.setPlayerBusinessType(PlayerBusinessType.SPEAK_MEETING);
					
					BusinessPlayerVO splitVO = new BusinessPlayerVO().set(player);
					splits.add(splitVO);
					
					//给转发设置目的
					c2m_forward.setDstPlayer(player);
					c2m_forward.setExecuteStatus(ExecuteStatus.UNDONE);//UNDONE不行就改成WAITING_FOR_RESPONSE
					player.setMember(member);
					usefulPlayersCount--;
				}else{
					c2m_forward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
				}				
			}
			member.getPlayers().addAll(players);
			needSavePlayers.addAll(players);
			//给进入会议的人
			if(member.getMemberStatus().equals(MemberStatus.CONNECT)){
				needPlayers.addAll(players);
				//生成推送信息			
				JSONObject message = new JSONObject();
				message.put("businessType", "speakStart");
				message.put("businessInfo", businessInfo);
				message.put("businessId", group.getId().toString());
				message.put("splits", splits);
				messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
		}
		
		group.getForwards().addAll(newForwards);
		allNeedForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, newForwards);
		
		commandGroupDao.save(group);
		commandGroupUserPlayerDao.save(needSavePlayers);
		
		//生成connectBundle，携带转发信息
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = commandBasicServiceImpl.openBundle(null, null, needPlayers, allNeedForwards, null, codec, chairmanMember.getUserNum());
		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, allNeedForwards, null, null, null, codec, group.getUserId());
		logic.merge(logicCastDevice);
		
		executeBusiness.execute(logic, group.getName() + " " + businessInfo + " " + speakMembers.size() + "个新发言人");
		
		//发消息
		for(MessageSendCacheBO cache : messageCaches){
			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	/**
	 * 停止发言<br/>
	 * <p>持久化、推送消息、呼叫收发流</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午9:11:41
	 * @param group
	 * @param speakMembers 停止发言人列表，必须是已经在发言的人
	 * @param mode 目前仅用于显示信息：0发言；1全员讨论
	 * @throws Exception
	 */
	private void speakStop(CommandGroupPO group, List<CommandGroupMemberPO> speakMembers, int mode) throws Exception{
		
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		List<CommandGroupForwardPO> forwards = group.getForwards();
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<Long> consumeIds = new ArrayList<Long>();
		List<Long> speakMemberIds = new ArrayList<Long>();
		
		//统计发言人名
		List<String> speakNameList = new ArrayList<String>();			
		for(CommandGroupMemberPO speakMember : speakMembers){
			speakMemberIds.add(speakMember.getId());
			speakMember.setCooperateStatus(MemberStatus.DISCONNECT);//是否需要先判断一下
			speakNameList.add(speakMember.getUserName());
		}
		String businessInfo = null;
		if(mode == 0){
			businessInfo = StringUtils.join(speakNameList.toArray(), "，") + " 停止发言";
		}else if(mode == 1){
			businessInfo = "停止讨论";
		}
		
		//以这些成员为源的协同转发
		Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, speakMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
		List<CommandGroupForwardPO> allNeedDelForwards = new ArrayList<CommandGroupForwardPO>();
		
		//释放这些退出或删除成员的播放器，同时如果是删人就给被删的成员发消息
		List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		//处理所有成员，包括被撤销的，主席不停转发不删播放器，发消息
		for(CommandGroupMemberPO member : members){
			
			if(OriginType.OUTER.equals(member.getOriginType())){
				continue;
			}
			
//			if(member.isAdministrator()){// || revokeMemberIds.contains(member.getId())){
//				continue;
//			}
			List<CommandGroupUserPlayerPO> thisMemberFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			JSONArray splits = new JSONArray();
			//主席不停转发不删播放器
			if(!member.isAdministrator()){
				for(CommandGroupForwardPO forward : relativeForwards){
					if(member.getId().equals(forward.getDstMemberId())){
						//目的是该成员的，找播放器
						for(CommandGroupUserPlayerPO player : member.getPlayers()){
							//播放器对应转发目的，且 这是一条协同转发，且 该转发的源是一个被撤销协同的成员
							if(player.getBundleId().equals(forward.getDstVideoBundleId())
									//&& forward.getForwardBusinessType().equals(ForwardBusinessType.COOPERATE_COMMAND)//这个条件上头查询已经有了
									&& speakMemberIds.contains(forward.getSrcMemberId())){
								player.setFree();
								needFreePlayers.add(player);
								thisMemberFreePlayers.add(player);
								allNeedDelForwards.add(forward);
								
								JSONObject split = new JSONObject();
								split.put("serial", player.getLocationIndex());
								splits.add(split);
							}
						}
					}
				}
			}
			member.getPlayers().removeAll(thisMemberFreePlayers);
			log.info(member.getUserName() + " 释放了播放器个数：" + thisMemberFreePlayers.size());
			
			JSONObject message = new JSONObject();
			message.put("businessType", "speakStop");
			message.put("businessId", group.getId().toString());
			message.put("businessInfo", businessInfo);
			message.put("splits", splits);
			messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
		}
		
		forwards.removeAll(allNeedDelForwards);
		log.info(businessInfo + "，删除转发个数：" + allNeedDelForwards.size());
		
		commandGroupDao.save(group);
		commandGroupUserPlayerDao.save(needFreePlayers);
		
		//发协议（删转发协议不用发，通过挂断播放器来删）
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needFreePlayers, codec, chairmanMember.getUserNum());
		LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needFreePlayers, codec, group.getUserId());
		logic.merge(logicCastDevice);
		
		//录制更新
		LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
		logic.merge(logicRecord);
		
		ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, businessInfo);
		commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());			

		//发消息
		for(MessageSendCacheBO cache : messageCaches){
			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
}
