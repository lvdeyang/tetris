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
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandMeetingServiceImpl 
* @Description: 指挥系统中的会议业务
* @author zsy
* @date 2020年1月3日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
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
	public void speakAppoint(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
			}
			
			if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(userIdArray.contains(group.getUserId())){
				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");
			}
						
			//发言人，校验是否已经在发言
			Set<CommandGroupMemberPO> members = group.getMembers();
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
			
			//校验协同成员是否入会
			for(CommandGroupMemberPO speakMember : speakMembers){
				if(!speakMember.getMemberStatus().equals(MemberStatus.CONNECT)){
					throw new BaseException(StatusCode.FORBIDDEN, speakMember.getUserName() + " 还未进入");
				}
			}
			
			//这里有持久化
			speakStart(group, speakMembers, 0);
		}
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
	public void speakApply(Long userId, Long groupId) throws Exception{

		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
			}
			
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要申请发言");
			}
			
			Set<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO member = commandCommonUtil.queryMemberByUserId(members, userId);
			if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, "您正在发言");
			}
			
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApply");
			message.put("businessInfo", member.getUserName() + "申请发言");			
			
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			WebsocketMessageVO ws = websocketMessageService.send(chairmanMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
			websocketMessageService.consume(ws.getId());
			
			log.info(group.getName() + "申请发言");
		}
	}
	
	/**
	 * 主席同意成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:34:35
	 * @param userId 操作人
	 * @param groupId
	 * @param userIdArray 被拒绝的用户
	 * @throws Exception
	 */
	public void speakApplyAgree(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
//			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
			}
			
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			//这个判断正常情况没有用
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要发言");
			}
						
			//发言人，校验是否已经在发言
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(userIdArray.contains(member.getUserId())){
					if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
						member.setCooperateStatus(MemberStatus.CONNECT);
						speakMembers.add(member);
					}else{
//						if(groupType.equals(GroupType.BASIC)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同会议");
//						}else if(groupType.equals(GroupType.MEETING)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 正在发言");
//						}
					}
				}
			}
			
			//校验发言人是否入会（正常情况下不需要）
			for(CommandGroupMemberPO speakMember : speakMembers){
				if(!speakMember.getMemberStatus().equals(MemberStatus.CONNECT)){
					throw new BaseException(StatusCode.FORBIDDEN, speakMember.getUserName() + " 还未进入");
				}
			}
			
			speakStart(group, speakMembers, 0);
		}
	}
	

	/**
	 * 主席拒绝成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午11:37:12
	 * @param userId 操作人
	 * @param groupId
	 * @param userIdArray 被同意的用户
	 * @throws Exception
	 */
	public void speakApplyDisagree(Long userId, Long groupId, List<Long> userIds) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			
			if(group.getStatus().equals(GroupStatus.STOP) || userIds.size()==0){
				return;
			}
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();			
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			JSONObject message = new JSONObject();
			message.put("businessType", "speakApplyDisagree");
			message.put("businessInfo", "主席拒绝了您的发言申请");
			message.put("businessId", group.getId().toString());
			for(CommandGroupMemberPO speakMember : speakMembers){
				messageCaches.add(new MessageSendCacheBO(speakMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
			}			
			
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			log.info(group.getName() + " 主席拒绝了 " + speakMembers.get(0).getUserName() + " 等人的发言申请");
		}
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
	public void speakStopByMember(Long userId, Long groupId) throws Exception{

		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
//			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
						
			//发言人，校验是否已经在发言
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			CommandGroupMemberPO speakMember = commandCommonUtil.queryMemberByUserId(members, userId);
			if(speakMember.getCooperateStatus().equals(MemberStatus.CONNECT)){
				speakMember.setCooperateStatus(MemberStatus.DISCONNECT);
				speakMembers.add(speakMember);
			}else{
				return;
			}
			
			//这里有持久化
			speakStop(group, speakMembers, 0);
		}
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
	public void speakStopByChairman(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				return;
			}
			if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行发言");
			}
			
			//正常情况不会出现
			if(userIdArray.contains(group.getUserId())){
//				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");
			}
						
			//发言人，校验是否已经在发言
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(userIdArray.contains(member.getUserId())){
					if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
						member.setCooperateStatus(MemberStatus.DISCONNECT);
						speakMembers.add(member);
					}else{
//						if(groupType.equals(GroupType.BASIC)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同会议");
//						}else if(groupType.equals(GroupType.MEETING)){
//							throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 正在发言");
//						}
					}
				}
			}
			
			//这里有持久化
			speakStop(group, speakMembers, 0);
		}
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
	public void discussStart(Long userId, Long groupId) throws Exception{
				
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			group.setSpeakType(GroupSpeakType.DISCUSS);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
			}
			if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
						
			//发言人，入会的，且没在发言的都进行发言
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(member.getMemberStatus().equals(MemberStatus.CONNECT)
						&& member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
					member.setCooperateStatus(MemberStatus.CONNECT);
					speakMembers.add(member);
				}
			}
			
			speakStart(group, speakMembers, 1);
		}
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
	public void discussStop(Long userId, Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			group.setSpeakType(GroupSpeakType.CHAIRMAN);
			GroupType groupType = group.getType();
			
			if(group.getStatus().equals(GroupStatus.STOP)){
//				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
				return;
			}
			if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "指挥中不能进行讨论");
			}
						
			//发言人，在发言的都停止
			Set<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> speakMembers = new ArrayList<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
					member.setCooperateStatus(MemberStatus.DISCONNECT);
					speakMembers.add(member);
				}
			}
			
			speakStop(group, speakMembers, 1);
		}
	}
	
	
	/**
	 * 开始发言<br/>
	 * <p>持久化、推送消息、呼叫收发流</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午6:01:15
	 * @param group
	 * @param speakMembers 发言人列表，必须是已经进会、且新增的发言人，不能已经在发言
	 * @param mode 0发言，1全员讨论
	 * @return
	 * @throws Exception
	 */
	private void speakStart(CommandGroupPO group, List<CommandGroupMemberPO> speakMembers, int mode) throws Exception{
		
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
		Set<CommandGroupMemberPO> members = group.getMembers();
		
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
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
		
		//除主席外，给每个成员查找speakMembers.size()个播放器，建立转发
		for(CommandGroupMemberPO member : members){
			
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
					
					player.setBusinessId(group.getId().toString());//如果需要改成c2m_forward.getId()，那么需要先save获得id
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
					c2m_forward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
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
		LogicBO logic = commandBasicServiceImpl.openBundle(speakMembers, null, needPlayers, allNeedForwards, null, codec, group.getUserId());
		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, allNeedForwards, null, null, null, codec, group.getUserId());
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
	 * @param mode 0发言，1全员讨论
	 * @throws Exception
	 */
	private void speakStop(CommandGroupPO group, List<CommandGroupMemberPO> speakMembers, int mode) throws Exception{
		
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		Set<CommandGroupForwardPO> forwards = group.getForwards();
		Set<CommandGroupMemberPO> members = group.getMembers();
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
		
		//除主席外，处理所有成员，包括被撤销的
		for(CommandGroupMemberPO member : members){
			if(member.isAdministrator()){// || revokeMemberIds.contains(member.getId())){
				continue;
			}
			List<CommandGroupUserPlayerPO> thisMemberFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			JSONArray splits = new JSONArray();
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
			member.getPlayers().removeAll(thisMemberFreePlayers);
			log.info(member.getUserName() + " 释放了播放器个数：" + thisMemberFreePlayers.size());
			
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationRevoke");
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
		LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needFreePlayers, codec, group.getUserId());
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
