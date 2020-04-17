package com.sumavision.bvc.device.command.cooperate;

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
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandCooperateServiceImpl 
* @Description: 协同会议业务
* @author zsy
* @date 2019年10月24日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandCooperateServiceImpl {
	
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
	private CommandCascadeService commandCascadeService;
	
	/**
	 * 
	 * 发起协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月29日 上午11:36:03
	 * @param groupId
	 * @param memberIdArray 协同成员id列表
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Object start_Deprecated(Long groupId, List<Long> userIdArray) throws Exception{
		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		GroupType groupType = group.getType();
		String commandString = commandCommonUtil.generateCommandString(groupType);
		if(groupType.equals(GroupType.MEETING)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
		}
//		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
		}
		
		if(userIdArray.contains(group.getUserId())){
			if(groupType.equals(GroupType.BASIC)){
				throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同" + commandString);
			}else if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");//无用
			}
		}
		
		//协同成员，校验是否已经被授权协同
		List<CommandGroupMemberPO> members = group.getMembers();
		Set<CommandGroupMemberPO> cooperateMembers = new HashSet<CommandGroupMemberPO>();
		for(CommandGroupMemberPO member : members){
			if(userIdArray.contains(member.getUserId())){
				if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
					cooperateMembers.add(member);
				}else{
					if(groupType.equals(GroupType.BASIC)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同" + commandString);
					}else if(groupType.equals(GroupType.MEETING)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经发言");//会议发言业务没有CONNECTING状态
					}
				}
			}
		}
		
		//校验协同成员是否入会
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			if(!cooperateMember.getMemberStatus().equals(MemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, cooperateMember.getUserName() + " 还未进入");
			}
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		List<CommandGroupForwardPO> forwards = group.getForwards();//new HashSet<CommandGroupForwardPO>();
		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
		
		//除主席外，给每个成员查找memberIdArray.size()个播放器，建立转发
		for(CommandGroupMemberPO member : members){
			
			if(member.isAdministrator()){
				continue;
			}
			
//			String cooStr = "";
//			int needPlayerCount = userIdArray.size();
//			if(userIdArray.contains(member.getUserId())){
//				//这是协同成员
//				needPlayerCount--;
//				cooStr = " 也是协同成员，";
//			}
//			
//			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needPlayerCount, false);
//			int usefulPlayersCount = players.size();
//			log.info(new StringBufferWrapper().append("协同成员数为 ").append(userIdArray.size())
//					.append("， ").append(member.getUserName()).append(cooStr).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
			
			for(CommandGroupMemberPO cooperateMember : cooperateMembers){
				
				//避免协同成员自己看自己
				if(member.getUuid().equals(cooperateMember.getUuid())){
					continue;
				}
				
				CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
						ForwardBusinessType.COOPERATE_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						member.getId(),
						cooperateMember.getId(),
						cooperateMember.getSrcBundleId(),
						cooperateMember.getSrcBundleName(),
						cooperateMember.getSrcVenusBundleType(),
						cooperateMember.getSrcLayerId(),
						cooperateMember.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						cooperateMember.getSrcBundleId(),
						cooperateMember.getSrcBundleName(),
						cooperateMember.getSrcBundleType(),
						cooperateMember.getSrcLayerId(),
						cooperateMember.getSrcAudioChannelId(),
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
				forwards.add(c2m_forward);
				
				//如果有播放器，则设置dst
//				if(usefulPlayersCount > 0){
//					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
//					
//					player.setBusinessId(group.getId().toString());//如果需要改成c2m_forward.getId()，那么需要先save获得id
//					if(groupType.equals(GroupType.BASIC)){
//						player.setBusinessName(group.getName() + "-" + cooperateMember.getUserName() + "协同" + commandString);
//					}if(groupType.equals(GroupType.MEETING)){
//						player.setBusinessName(group.getName() + "-" + cooperateMember.getUserName() + "发言");//无用
//					}
//					player.setPlayerBusinessType(PlayerBusinessType.COOPERATE_COMMAND);
//					
//					//用于返回的分屏信息
//					JSONObject split = new JSONObject();
//					split.put("serial", player.getLocationIndex());
//					split.put("bundleId", player.getBundleId());
//					split.put("bundleNo", player.getCode());
//					split.put("businessType", "command");//TODO:存疑，cooperation，player.getPlayerBusinessType().getCode()
//					split.put("businessId", group.getId().toString());
//					split.put("businessInfo", player.getBusinessName());
//					split.put("status", "start");
//					chairSplits.add(split);
//					
//					//给转发设置目的
//					c2m_forward.setDstPlayer(player);
//					c2m_forward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
//					player.setMember(member);
//					usefulPlayersCount--;
//				}else{
//					c2m_forward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
//				}				
			}
//			member.getPlayers().addAll(players);
//			needSavePlayers.addAll(players);
		}

		//给每个协同成员发送消息
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			
			cooperateMember.setCooperateStatus(MemberStatus.CONNECTING);
			
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationGrant");
			message.put("fromUserId", chairman.getUserId());
			message.put("fromUserName", chairman.getUserName());
			//TODO: businessId中添加其他信息
			message.put("businessId", group.getId().toString());
			if(groupType.equals(GroupType.BASIC)){
				message.put("businessInfo", "接受到 " + group.getName() + " 的主席：" + chairman.getUserName() + " 邀请进入协同" + commandString + "，是否进入？");
			}else if(groupType.equals(GroupType.MEETING)){
				message.put("businessInfo", "接受到 " + group.getName() + " 的主席：" + chairman.getUserName() + " 指定发言，是否同意？");//无用
			}
			
			WebsocketMessageVO ws = websocketMessageService.send(cooperateMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
			cooperateMember.setMessageCoId(ws.getId());
		}
		
//		group.getForwards().addAll(forwards);		
		
		commandGroupDao.save(group);
//		commandGroupUserPlayerDao.save(needSavePlayers);
		
		//自动接听测试代码
//		List<CommandGroupMemberPO> acceptMembers = new ArrayList<CommandGroupMemberPO>();
//		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
//			acceptMembers.add(cooperateMember);
//		}
//		membersResponse(group, acceptMembers, null);

		}
		operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，userIds:" + userIdArray.toString());
		return chairSplits;
	}
	
	/**
	 * 
	 * 发起协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月29日 上午11:36:03
	 * @param groupId
	 * @param memberIdArray 协同成员id列表
	 * @return
	 * @throws Exception
	 */
	public Object start(Long groupId, List<Long> userIdArray) throws Exception{
		UserVO user = userQuery.current();
		JSONArray chairSplits = new JSONArray();
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		GroupType groupType = group.getType();
		String commandString = commandCommonUtil.generateCommandString(groupType);
		if(groupType.equals(GroupType.MEETING)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
		}
//		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
		}
		
		if(userIdArray.contains(group.getUserId())){
			if(groupType.equals(GroupType.BASIC)){
				throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同" + commandString);
			}else if(groupType.equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, "不能指定主席发言");//无用
			}
		}
		
		//协同成员，校验是否已经被授权协同
		List<CommandGroupMemberPO> members = group.getMembers();
		List<CommandGroupMemberPO> cooperateMembers = new ArrayList<CommandGroupMemberPO>();
		for(CommandGroupMemberPO member : members){
			if(userIdArray.contains(member.getUserId())){
				if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
					cooperateMembers.add(member);
				}else{
					if(groupType.equals(GroupType.BASIC)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同" + commandString);
					}else if(groupType.equals(GroupType.MEETING)){
						throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经发言");//会议发言业务没有CONNECTING状态
					}
				}
			}
		}
		
		//校验协同成员是否入会
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			if(!cooperateMember.getMemberStatus().equals(MemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, cooperateMember.getUserName() + " 还未进入");
			}
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		List<CommandGroupForwardPO> forwards = group.getForwards();//new HashSet<CommandGroupForwardPO>();
		
		//除主席外，给每个成员建立转发
		for(CommandGroupMemberPO member : members){
			
			if(member.isAdministrator()){
				continue;
			}
			
			for(CommandGroupMemberPO cooperateMember : cooperateMembers){
				
				//避免协同成员自己看自己
				if(member.getUuid().equals(cooperateMember.getUuid())){
					continue;
				}
				//查找这个member看这个cooperateMember是否已经存在
				CommandGroupForwardPO existedForward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, cooperateMember.getId(), member.getId());
				if(existedForward != null){
					continue;
				}
				
				CommandGroupForwardPO c2m_forward = new CommandGroupForwardPO(
						ForwardBusinessType.COOPERATE_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						member.getId(),
						cooperateMember.getId(),
						cooperateMember.getSrcBundleId(),
						cooperateMember.getSrcBundleName(),
						cooperateMember.getSrcVenusBundleType(),
						cooperateMember.getSrcLayerId(),
						cooperateMember.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						cooperateMember.getSrcBundleId(),
						cooperateMember.getSrcBundleName(),
						cooperateMember.getSrcBundleType(),
						cooperateMember.getSrcLayerId(),
						cooperateMember.getSrcAudioChannelId(),
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
				forwards.add(c2m_forward);		
			}
		}
		
		//每个协同成员看其他所有人
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			for(CommandGroupMemberPO member : members){
				if(member.isAdministrator()){
					continue;
				}
				//避免协同成员自己看自己
				if(member.getUuid().equals(cooperateMember.getUuid())){
					continue;
				}
				//查找这个cooperateMember看这个member是否已经存在
				CommandGroupForwardPO existedForward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, member.getId(), cooperateMember.getId());
				if(existedForward != null){
					continue;
				}
				
				CommandGroupForwardPO m2c_forward = new CommandGroupForwardPO(
						ForwardBusinessType.COOPERATE_COMMAND,
						ExecuteStatus.UNDONE,
						ForwardDstType.USER,
						cooperateMember.getId(),
						member.getId(),
						member.getSrcBundleId(),
						member.getSrcBundleName(),
						member.getSrcVenusBundleType(),
						member.getSrcLayerId(),
						member.getSrcVideoChannelId(),
						"VenusVideoIn",//videoBaseType,
						member.getSrcBundleId(),
						member.getSrcBundleName(),
						member.getSrcBundleType(),
						member.getSrcLayerId(),
						member.getSrcAudioChannelId(),
						"VenusAudioIn",//String audioBaseType,
						null,//cooperateMember.getDstBundleId(),
						null,//cooperateMember.getDstBundleName(),
						null,//cooperateMember.getDstBundleType(),
						null,//cooperateMember.getDstLayerId(),
						null,//cooperateMember.getDstVideoChannelId(),
						"VenusVideoOut",//String dstVideoBaseType,
						null,//cooperateMember.getDstAudioChannelId(),
						null,//cooperateMember.getDstBundleName(),
						null,//cooperateMember.getDstBundleType(),
						null,//cooperateMember.getDstLayerId(),
						null,//cooperateMember.getDstAudioChannelId(),
						"VenusAudioOut",//String dstAudioBaseType,
						group.getUserId(),
						group.getAvtpl().getId(),//g_avtpl.getId(),//Long avTplId,
						currentGear.getId(),//Long gearId,
						DstDeviceType.WEBSITE_PLAYER,
						null,//LiveType type,
						null,//Long osdId,
						null//String osdUsername);
						);
				m2c_forward.setGroup(group);
				forwards.add(m2c_forward);				
			}
		}
		
		commandGroupDao.save(group);
		
		//级联
		GroupType type = group.getType();
		if(!OriginType.OUTER.equals(group.getOriginType())){
			if(GroupType.BASIC.equals(type)){
				GroupBO groupBO = commandCascadeUtil.startCooperation(group, cooperateMembers);
				commandCascadeService.startCooperation(groupBO);
			}
		}
		
		membersResponse(group, cooperateMembers, null);
		
		operationLogService.send(user.getNickname(), "开启协同指挥", user.getNickname() + "开启协同指挥，groupId:" + groupId + "，userIds:" + userIdArray.toString());
		return chairSplits;
		}
	}

	/**
	 * 同意协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:48:42
	 * @param userId
	 * @param groupId
	 * @throws Exception
	 */
	public void agree(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
		}
		
		if(group.getType().equals(GroupType.MEETING)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
		}
		
		CommandGroupMemberPO acceptMember = commandCommonUtil.queryMemberByUserId(group.getMembers(), userId);
		MemberStatus cooperateStatus = acceptMember.getCooperateStatus();
		if(cooperateStatus.equals(MemberStatus.CONNECT)){
			return;
		}else if(cooperateStatus.equals(MemberStatus.DISCONNECT)){
			if(group.getType().equals(GroupType.BASIC)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "协同已结束");
			}else if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "发言已结束");//无用
			}
			
		}
		
		List<CommandGroupMemberPO> acceptMembers = new ArrayListWrapper<CommandGroupMemberPO>().add(acceptMember).getList();
		
		membersResponse(group, acceptMembers, null);
		
		}
		operationLogService.send(user.getNickname(), "同意协同指挥", user.getNickname() + "同意协同指挥，groupId:" + groupId);
	}
	
	/**
	 * 拒绝协同会议<br/>
	 * <p>线程不安全，调用处必须使用 command-group-{groupId} 加锁</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:48:57
	 * @param userId
	 * @param groupId
	 * @throws Exception
	 */
	public void refuse(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		if(group.getType().equals(GroupType.MEETING)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
		}
		CommandGroupMemberPO refuseMember = commandCommonUtil.queryMemberByUserId(group.getMembers(), userId);
		MemberStatus cooperateStatus = refuseMember.getCooperateStatus();
		if(cooperateStatus.equals(MemberStatus.CONNECT)){
			if(group.getType().equals(GroupType.BASIC)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "正在进行协同");
			}else if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "正在发言");//无用
			}
			
		}else if(cooperateStatus.equals(MemberStatus.DISCONNECT)){
			return;
		}
		
		List<CommandGroupMemberPO> refuseMembers = new ArrayListWrapper<CommandGroupMemberPO>().add(refuseMember).getList();
		
		membersResponse(group, null, refuseMembers);
		
		}
		operationLogService.send(user.getNickname(), "拒绝协同指挥", user.getNickname() + "拒绝协同指挥groupId:" + groupId);
	}
	
	/**
	 * 撤销授权协同会议<br/>
	 * <p>单个接口已弃用，使用批量revokeBatch</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月4日 下午1:26:41
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public JSONArray revoke(Long userId, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
			}
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
			}
		
			String commandString = commandCommonUtil.generateCommandString(group.getType());
			JSONArray groupInfos = new JSONArray();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO revokeMember = commandCommonUtil.queryMemberByUserId(members, userId);
			CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
			List<Long> consumeIds = new ArrayList<Long>();
			
			if(revokeMember.getMessageCoId() != null){
//				websocketMessageService.consume(revokeMember.getMessageCoId());
				consumeIds.add(revokeMember.getMessageCoId());
				revokeMember.setMessageCoId(null);
			}
			
			revokeMember.setCooperateStatus(MemberStatus.DISCONNECT);
			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(revokeMember.getId()).getList();
			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
			
			//给正在观看的成员发送的消息
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationRevoke");
			//TODO: businessId中添加其他信息
			message.put("businessId", group.getId().toString());
			message.put("splits", new JSONArray());
			if(group.getType().equals(GroupType.BASIC)){
				message.put("businessInfo", group.getName() + " 主席撤销授权 " + revokeMember.getUserName() + " 协同" + commandString);
			}else if(group.getType().equals(GroupType.MEETING)){
				message.put("businessInfo", group.getName() + " 主席停止 " + revokeMember.getUserName() + " 发言");//无用
			}
			
			//给被撤销成员发消息 TODO:信息可能不足
			messageCaches.add(new MessageSendCacheBO(revokeMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName()));
//			WebsocketMessageVO ws = websocketMessageService.send(revokeMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
//			consumeIds.add(ws.getId());
			
			//释放其他成员的播放器，删除转发PO
			List<CommandGroupUserPlayerPO> needClosePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupForwardPO forward : relativeForwards){
				forward.setGroup(null);
				//TODO:释放播放器没做好
//				if(forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
					CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
					JSONArray splits = new JSONArray();
					message.put("splits", splits);
					for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
						if(player.getBundleId().equals(forward.getDstVideoBundleId())
								&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
							
							//forward状态是DONE的，close播放器
//							if(forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
								needClosePlayers.add(player);
//							}
							
							player.setFree();
							needFreePlayers.add(player);
							dstMember.getPlayers().remove(player);
							
							//给正在观看的成员填入分屏信息，发消息
							JSONObject split = new JSONObject();
							split.put("serial", player.getLocationIndex());
							splits.add(split);
						}
					}
					messageCaches.add(new MessageSendCacheBO(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName()));
//					WebsocketMessageVO ws2 = websocketMessageService.send(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
//					consumeIds.add(ws2.getId());
//				}
			}
			forwards.removeAll(relativeForwards);
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(needFreePlayers);
			
			//关闭播放器，考虑怎么给不需要挂断的编码器计数
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needClosePlayers, codec, chairman.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needClosePlayers, codec, group.getUserId());
			logic.merge(logicCastDevice);
			executeBusiness.execute(logic, group.getName() + " 会议中撤销" + revokeMember.getUserName() + "的协同会议授权");
			
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + "，userId:" + userId);
			return groupInfos;		
		}
	}

	/**
	 * 批量撤销授权协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午6:05:37
	 * @param userIds
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public JSONArray revokeBatch_Deprecated(List<Long> userIds, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
			}
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
			}
		
			String commandString = commandCommonUtil.generateCommandString(group.getType());
			JSONArray groupInfos = new JSONArray();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> revokeMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<Long> consumeIds = new ArrayList<Long>();
			List<Long> revokeMemberIds = new ArrayList<Long>();
			
			StringBufferWrapper revokeMembersNames = new StringBufferWrapper();			
			for(CommandGroupMemberPO revokeMember : revokeMembers){
				if(revokeMember.getMessageCoId() != null){
					consumeIds.add(revokeMember.getMessageCoId());
					revokeMember.setMessageCoId(null);
				}
				revokeMemberIds.add(revokeMember.getId());
				revokeMember.setCooperateStatus(MemberStatus.DISCONNECT);//TODO:是否需要先判断一下
				revokeMembersNames.append(revokeMember.getUserName()).append("，");
			}
			revokeMembersNames.append("被主席撤销协同" + commandString);	
			
			//以这些成员为源的协同转发
			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, revokeMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
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
									&& revokeMemberIds.contains(forward.getSrcMemberId())){
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
				message.put("businessInfo", revokeMembersNames.toString());
				message.put("splits", splits);
				messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
			}
			
			forwards.removeAll(allNeedDelForwards);
			log.info(revokeMembersNames.toString() + "，删除转发个数：" + allNeedDelForwards.size());
			
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
			
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, revokeMembersNames.toString());
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());			

			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);			
			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", userIds:" + userIds.toString());
			return groupInfos;
		}
	}
	
	/**
	 * 批量撤销授权协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午6:05:37
	 * @param userIds
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public JSONArray revokeBatch(List<Long> userIds, Long groupId) throws Exception{
		UserVO user = userQuery.current();
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已经停止。id: " + group.getId());
			}
			if(group.getType().equals(GroupType.MEETING)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "会议中不能进行协同");
			}
		
			String commandString = commandCommonUtil.generateCommandString(group.getType());
			JSONArray groupInfos = new JSONArray();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupMemberPO> revokeMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			List<Long> consumeIds = new ArrayList<Long>();
			List<Long> revokeMemberIds = new ArrayList<Long>();
			
			StringBufferWrapper revokeMembersNames = new StringBufferWrapper();			
			for(CommandGroupMemberPO revokeMember : revokeMembers){
				if(revokeMember.getMessageCoId() != null){
					consumeIds.add(revokeMember.getMessageCoId());
					revokeMember.setMessageCoId(null);
				}
				revokeMemberIds.add(revokeMember.getId());
				revokeMember.setCooperateStatus(MemberStatus.DISCONNECT);//TODO:是否需要先判断一下
				revokeMembersNames.append(revokeMember.getUserName()).append("，");
			}
			revokeMembersNames.append("被主席撤销协同" + commandString);	
			
			//以这些成员为源或目的的协同转发
			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsByMemberIds(forwards, revokeMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
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
							if(player.getBundleId().equals(forward.getDstVideoBundleId())){
									//&& forward.getForwardBusinessType().equals(ForwardBusinessType.COOPERATE_COMMAND)//这个条件上头查询已经有了
//									&& revokeMemberIds.contains(forward.getSrcMemberId())){
								
								//源和目的都不是协同，才释放
								if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
									CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
									if(srcMember.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
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
				}
				member.getPlayers().removeAll(thisMemberFreePlayers);
				log.info(member.getUserName() + " 释放了播放器个数：" + thisMemberFreePlayers.size());
				
				JSONObject message = new JSONObject();
				message.put("businessType", "cooperationRevoke");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", revokeMembersNames.toString());
				message.put("splits", splits);
				messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName()));
			}
			
			forwards.removeAll(allNeedDelForwards);
			log.info(revokeMembersNames.toString() + "，删除转发个数：" + allNeedDelForwards.size());
			
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(needFreePlayers);
			
			GroupType type = group.getType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(type)){
					GroupBO groupBO = commandCascadeUtil.stopCooperation(group, revokeMembers);
					commandCascadeService.stopCooperation(groupBO);
				}
			}
			
			//发协议（删转发协议不用发，通过挂断播放器来删）
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needFreePlayers, codec, chairmanMember.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needFreePlayers, codec, group.getUserId());
			logic.merge(logicCastDevice);
			
			//录制更新
			LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
			logic.merge(logicRecord);
			
			ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, revokeMembersNames.toString());
			commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());			
	
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);			
			operationLogService.send(user.getNickname(), "撤销协同指挥", user.getNickname() + "撤销协同指挥groupId:" + groupId + ", userIds:" + userIds.toString());
			return groupInfos;
		}
	}

	/**
	 * 给[入会成员]选择观看协同的播放器，给协同成员选择观看[入会成员]的播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午9:58:52
	 * @param group
	 * @param cooperateMembers
	 * @throws Exception
	 */
	@Deprecated
	private void chosePlayersForMembers_Deprecated(CommandGroupPO group, List<CommandGroupMemberPO> cooperateMembers) throws Exception{
		
		if(cooperateMembers==null || cooperateMembers.size()==0){
			return;
		}
		
		List<CommandGroupMemberPO> members = group.getMembers();
		List<CommandGroupForwardPO> forwards = group.getForwards();
		GroupType groupType = group.getType();
		String commandString = commandCommonUtil.generateCommandString(groupType);
		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		//除主席外，给每个[入会]成员查找memberIdArray.size()个播放器，建立转发
		for(CommandGroupMemberPO member : members){
			
			if(member.isAdministrator() || !MemberStatus.CONNECT.equals(member.getMemberStatus())){
				continue;
			}
			String cooStr = "";
			int needPlayerCount = cooperateMembers.size();
			CommandGroupMemberPO thisMember = commandCommonUtil.queryMemberById(cooperateMembers, member.getId());
			if(thisMember != null){
				//这是协同成员
				needPlayerCount--;
				cooStr = " 也是协同成员，";
			}
			
			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needPlayerCount, false);
			int usefulPlayersCount = players.size();
			log.info(new StringBufferWrapper().append("协同成员数为 ").append(cooperateMembers.size())
					.append("， ").append(member.getUserName()).append(cooStr).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
			
			for(CommandGroupMemberPO cooperateMember : cooperateMembers){				
				
				//避免协同成员自己看自己
				if(member.getUuid().equals(cooperateMember.getUuid())){
					continue;
				}
				
				//TODO:找到CommandGroupForwardPO
				CommandGroupForwardPO forward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, cooperateMember.getId(), member.getId());
				if(forward == null){
					continue;//正常不会是null
				}
				
				//如果有播放器，则设置dst
				if(usefulPlayersCount > 0){
					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
					
					player.setBusinessId(group.getId().toString());//如果需要改成c2m_forward.getId()，那么需要先save获得id
					if(groupType.equals(GroupType.BASIC)){
						player.setBusinessName(group.getName() + "-" + cooperateMember.getUserName() + "协同" + commandString);
					}if(groupType.equals(GroupType.MEETING)){
						player.setBusinessName(group.getName() + "-" + cooperateMember.getUserName() + "发言");//无用
					}
					player.setPlayerBusinessType(PlayerBusinessType.COOPERATE_COMMAND);
					
//					//用于返回的分屏信息
//					JSONObject split = new JSONObject();
//					split.put("serial", player.getLocationIndex());
//					split.put("bundleId", player.getBundleId());
//					split.put("bundleNo", player.getCode());
//					split.put("businessType", "command");//TODO:存疑，cooperation，player.getPlayerBusinessType().getCode()
//					split.put("businessId", group.getId().toString());
//					split.put("businessInfo", player.getBusinessName());
//					split.put("status", "start");
//					chairSplits.add(split);
					
					//给转发设置目的
					forward.setDstPlayer(player);
					forward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
					player.setMember(member);
					usefulPlayersCount--;
				}else{
					forward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
				}				
			}
			member.getPlayers().addAll(players);
			needSavePlayers.addAll(players);
		}
			
		commandGroupDao.save(group);
		commandGroupUserPlayerDao.save(needSavePlayers);
	}
	
	/**
	 * 给[入会成员]选择观看协同的播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午9:58:52
	 * @param group
	 * @param cooperateMembers
	 * @throws Exception
	 */
	private void chosePlayersForMembers(CommandGroupPO group, List<CommandGroupMemberPO> cooperateMembers) throws Exception{
		
		if(cooperateMembers==null || cooperateMembers.size()==0){
			return;
		}
		
		List<CommandGroupMemberPO> members = group.getMembers();
		List<CommandGroupForwardPO> forwards = group.getForwards();
		Set<CommandGroupForwardPO> forwardsReadyToBeDone = commandCommonUtil.queryForwardsReadyToBeDone(members, forwards);
		GroupType groupType = group.getType();
//		String commandString = commandCommonUtil.generateCommandString(groupType);
		List<CommandGroupUserPlayerPO> needSavePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		//除主席外，给每个[入会]成员统计需要的播放器个数，查找播放器，更新转发
		for(CommandGroupMemberPO member : members){
			
			if(OriginType.OUTER.equals(member.getOriginType())){
				continue;
			}
			
			if(member.isAdministrator() || !MemberStatus.CONNECT.equals(member.getMemberStatus())){
				continue;
			}
			
			//以该member为目的，保证了源成员都已进会的，协同转发
			List<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsByDstmemberIds(
					forwardsReadyToBeDone, new ArrayListWrapper<Long>().add(member.getId()).getList(),
					ForwardBusinessType.COOPERATE_COMMAND, ExecuteStatus.UNDONE);
			
			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needForwards.size(), false);
			int usefulPlayersCount = players.size();
			log.info(new StringBufferWrapper().append("需要的播放器数为 ").append(needForwards.size())
					.append("， ").append(member.getUserName()).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
			
			for(CommandGroupForwardPO needForward : needForwards){
				//如果有播放器，则设置dst
				if(usefulPlayersCount > 0){
					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
					CommandGroupMemberPO cooperateMember = commandCommonUtil.queryMemberById(members, needForward.getSrcMemberId());					
					player.setBusinessId(group.getId() + "-" + cooperateMember.getUserId());
					if(groupType.equals(GroupType.BASIC)){
						player.setBusinessName(group.getName() + "：" + cooperateMember.getUserName());// + "协同" + commandString);
					}if(groupType.equals(GroupType.MEETING)){
						player.setBusinessName(group.getName() + "：" + cooperateMember.getUserName());// + "发言");//无用
					}
					player.setPlayerBusinessType(PlayerBusinessType.COOPERATE_COMMAND);
					
					//给转发设置目的
					needForward.setDstPlayer(player);
					needForward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
					player.setMember(member);
					usefulPlayersCount--;
				}else{
					needForward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
				}				
			}
			member.getPlayers().addAll(players);
			needSavePlayers.addAll(players);
		}
			
		commandGroupDao.save(group);
		commandGroupUserPlayerDao.save(needSavePlayers);
	}

	/**
	 * 
	 * 批量处理协同成员的“接听”和“拒绝”<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 上午10:01:36
	 * @param group
	 * @param acceptMembers
	 * @param refuseMembers
	 * @throws Exception
	 */
	@Deprecated
	private void membersResponse_Deprecated(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers, List<CommandGroupMemberPO> refuseMembers) throws Exception{
					
			GroupType groupType = group.getType();
			String commandString = commandCommonUtil.generateCommandString(groupType);
			if(null == acceptMembers) acceptMembers = new ArrayList<CommandGroupMemberPO>();
			if(null == refuseMembers) refuseMembers = new ArrayList<CommandGroupMemberPO>();
			
			chosePlayersForMembers(group, acceptMembers);
			
			List<CommandGroupMemberPO> members = group.getMembers();
			List<CommandGroupForwardPO> forwards = group.getForwards();
			CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			//考虑如果停会之后执行，有没有问题
			
			//消息消费
			try {
				for(CommandGroupMemberPO acceptMember : acceptMembers){
					if(acceptMember.getMessageCoId() != null){
						consumeIds.add(acceptMember.getMessageCoId());
						acceptMember.setMessageCoId(null);
					}
				}
				for(CommandGroupMemberPO refuseMember : refuseMembers){
					if(refuseMember.getMessageCoId() != null){
						consumeIds.add(refuseMember.getMessageCoId());
						refuseMember.setMessageCoId(null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//判断是否在进行
			if(GroupStatus.STOP.equals(group.getStatus()) || GroupStatus.PAUSE.equals(group.getStatus())) {
				return;
			}
			
			//处理同意用户，呼叫转发目标成员的播放器；给其他成员发消息
			List<Long> acceptMemberIds = new ArrayList<Long>();//这个变量好像没用
			List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			Set<CommandGroupForwardPO> allNeedForwards = new HashSet<CommandGroupForwardPO>();
			for(CommandGroupMemberPO acceptMember : acceptMembers){
				acceptMember.setCooperateStatus(MemberStatus.CONNECT);
				acceptMemberIds.add(acceptMember.getId());
			}
			
			//从每个观看成员遍历（效率稍低，但是可以一次性获得一个成员所有观看的协同）
			for(CommandGroupMemberPO member : members){				
				if(member.isAdministrator()){
					continue;
				}				
				for(CommandGroupMemberPO acceptMember : acceptMembers){
					
					//避免协同成员自己看自己
					if(member.getUuid().equals(acceptMember.getUuid())){
						continue;
					}
					
					//查找播放器：bundleId与forward的目的相同，且playerBusinessType为COOPERATE_COMMAND
					List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(acceptMember.getId()).getList();
					Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
					Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, relativeForwards);
					for(CommandGroupForwardPO forward : needForwards){
						if(forward.getDstMemberId().equals(member.getId())){
							forward.setExecuteStatus(ExecuteStatus.DONE);
							allNeedForwards.add(forward);
//							CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
							for(CommandGroupUserPlayerPO player : member.getPlayers()){
								if(player.getBundleId().equals(forward.getDstVideoBundleId())
										&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
									needPlayers.add(player);
									
									JSONObject message = new JSONObject();
									message.put("businessType", "cooperationAgree");
									message.put("businessId", group.getId().toString());
									if(groupType.equals(GroupType.BASIC)){
										message.put("businessInfo", group.getName() + " 主席授权 " + acceptMember.getUserName() + " 协同" + commandString);
									}else if(groupType.equals(GroupType.MEETING)){
										message.put("businessInfo", group.getName() + " 成员 " + acceptMember.getUserName() + " 发言");//无用
									}
									
									message.put("splits", new JSONArray());
									//用于返回的分屏信息
									JSONObject split = new JSONObject();
									split.put("serial", player.getLocationIndex());
									split.put("bundleId", player.getBundleId());
									split.put("bundleNo", player.getCode());
									split.put("businessType", "cooperation");
									split.put("businessId", group.getId() + "-" + acceptMember.getUserId());
									if(groupType.equals(GroupType.BASIC)){
										split.put("businessInfo", group.getName() + "-" + acceptMember.getUserName() + "协同" + commandString);
									}else if(groupType.equals(GroupType.MEETING)){
										split.put("businessInfo", group.getName() + "-" + acceptMember.getUserName() + "发言");
									}
									split.put("status", "start");
									message.getJSONArray("splits").add(split);
									
//									WebsocketMessageVO ws = websocketMessageService.send(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, acceptMember.getUserId(), acceptMember.getUserName());
//									consumeIds.add(ws.getId());
									messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, acceptMember.getUserId(), acceptMember.getUserName()));
									
									break;
								}
							}
						}
					}
				}
			}
			
//			//从每个接受的协同成员遍历
//			for(CommandGroupMemberPO acceptMember : acceptMembers){				
//				//查找播放器：bundleId与forward的目的相同，且playerBusinessType为COOPERATE_COMMAND
//				List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(acceptMember.getId()).getList();
//				Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
//				Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, relativeForwards);
//				for(CommandGroupForwardPO forward : needForwards){
//						forward.setExecuteStatus(ExecuteStatus.DONE);
//						allNeedForwards.add(forward);
//						CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
//						for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
//							if(player.getBundleId().equals(forward.getDstVideoBundleId())
//									&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
//								needPlayers.add(player);
//								
//								JSONObject message = new JSONObject();
//								message.put("businessType", "cooperationAgree");
//								message.put("businessId", group.getId().toString());
//								message.put("businessInfo", group.getName() + " 会议主席授权 " + acceptMember.getUserName() + " 协同会议");
//								message.put("splits", new JSONArray());
//								//用于返回的分屏信息
//								JSONObject split = new JSONObject();
//								split.put("serial", player.getLocationIndex());
//								split.put("bundleId", player.getBundleId());
//								split.put("bundleNo", player.getCode());
//								split.put("businessType", "cooperation");
//								split.put("businessId", group.getId() + "-" + acceptMember.getId());
//								split.put("businessInfo", group.getName() + " 会议-" + acceptMember.getUserName() + "协同会议");
//								split.put("status", "start");
//								message.getJSONArray("splits").add(split);
//								
//								WebsocketMessageVO ws = websocketMessageService.send(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, acceptMember.getUserId(), acceptMember.getUserName());
//								consumeIds.add(ws.getId());
//								
//								break;
//							}
//						}
//				}
//			}
			
			//处理拒绝用户，释放其他成员的播放器，删除转发PO：查找该成员为源的转发，状态是UNDONE的，查找目的播放器；给主席发消息
			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupMemberPO refuseMember : refuseMembers){
				refuseMember.setCooperateStatus(MemberStatus.DISCONNECT);
				List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(refuseMember.getId()).getList();
				Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
				
				for(CommandGroupForwardPO forward : relativeForwards){
					forward.setGroup(null);
					if(forward.getExecuteStatus().equals(ExecuteStatus.UNDONE)
							|| forward.getExecuteStatus().equals(ExecuteStatus.DONE)){//UNDONE表示找到了播放器，此时应该不会出现DONE
						CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
						for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
							if(player.getBundleId().equals(forward.getDstVideoBundleId()) 
									&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
								player.setFree();
								needFreePlayers.add(player);
								dstMember.getPlayers().remove(player);
							}
						}
					}
				}
				forwards.removeAll(relativeForwards);
				
				JSONObject message = new JSONObject();
				message.put("businessType", "cooperationRefuse");
				//TODO: businessId中添加其他信息
				message.put("businessId", group.getId().toString());
				if(groupType.equals(GroupType.BASIC)){
					message.put("businessInfo", refuseMember.getUserName() + " 拒绝与您协同" + commandString);
				}else if(groupType.equals(GroupType.MEETING)){
					message.put("businessInfo", refuseMember.getUserName() + " 拒绝发言");//无用
				}
				
//				WebsocketMessageVO ws = websocketMessageService.send(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName());
//				consumeIds.add(ws.getId());
				messageCaches.add(new MessageSendCacheBO(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName()));
			}
			
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(needFreePlayers);
			
			//生成connectBundle，携带转发信息
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.openBundle(null, null, needPlayers, allNeedForwards, null, codec, chairman.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, allNeedForwards, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			
			executeBusiness.execute(logic, group.getName() + " 协同会议成员接听和拒绝");
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
	}

	/**
	 * 
	 * 批量处理协同成员的“接听”和“拒绝”<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 上午10:01:36
	 * @param group
	 * @param acceptMembers
	 * @param refuseMembers 废弃参数
	 * @throws Exception
	 */
	private void membersResponse(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers, List<CommandGroupMemberPO> refuseMembers) throws Exception{
					
		GroupType groupType = group.getType();
		String commandString = commandCommonUtil.generateCommandString(groupType);
		if(null == acceptMembers) acceptMembers = new ArrayList<CommandGroupMemberPO>();
		if(null == refuseMembers) refuseMembers = new ArrayList<CommandGroupMemberPO>();
		
		//处理同意用户，呼叫转发目标成员的播放器；给其他成员发消息
//		List<Long> acceptMemberIds = new ArrayList<Long>();//这个变量好像没用
		List<String> acceptMemberNamesList = new ArrayList<String>();
//		List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();//不用了
//		Set<CommandGroupForwardPO> allNeedForwards = new HashSet<CommandGroupForwardPO>();
		for(CommandGroupMemberPO acceptMember : acceptMembers){
			acceptMember.setCooperateStatus(MemberStatus.CONNECT);
			acceptMemberNamesList.add(acceptMember.getUserName());
//			acceptMemberIds.add(acceptMember.getId());
		}
		//save CONNECT状态
		commandGroupDao.save(group);
		
		String acceptMemberNames = StringUtils.join(acceptMemberNamesList.toArray(), ",");
		
		chosePlayersForMembers(group, acceptMembers);
		
		List<CommandGroupMemberPO> members = group.getMembers();
		List<CommandGroupForwardPO> forwards = group.getForwards();
		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
		List<Long> consumeIds = new ArrayList<Long>();
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		
		//考虑如果停会之后执行，有没有问题
		
		//消息消费
		try {
			for(CommandGroupMemberPO acceptMember : acceptMembers){
				if(acceptMember.getMessageCoId() != null){
					consumeIds.add(acceptMember.getMessageCoId());
					acceptMember.setMessageCoId(null);
				}
			}
			for(CommandGroupMemberPO refuseMember : refuseMembers){
				if(refuseMember.getMessageCoId() != null){
					consumeIds.add(refuseMember.getMessageCoId());
					refuseMember.setMessageCoId(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//判断是否在进行
		if(GroupStatus.STOP.equals(group.getStatus()) || GroupStatus.PAUSE.equals(group.getStatus())) {
			return;
		}
		
		//直接取出所有可执行的转发
		Set<CommandGroupForwardPO> forwardsReadyAndCanBeDone = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, forwards);
		
		//给除主席外的其它进会成员，生成消息
		for(CommandGroupMemberPO member : members){
		
			if(OriginType.OUTER.equals(member.getOriginType())){
				continue;
			}
			
			if(member.isAdministrator() || !MemberStatus.CONNECT.equals(member.getMemberStatus())){
				continue;
			}
			
			JSONObject message = new JSONObject();
			message.put("businessType", "cooperationAgree");
			message.put("businessId", group.getId().toString());
			if(groupType.equals(GroupType.BASIC)){
				message.put("businessInfo", group.getName() + " 主席授权 " + acceptMemberNames + " 协同" + commandString);
			}else if(groupType.equals(GroupType.MEETING)){
				message.put("businessInfo", group.getName() + " 成员 " + acceptMemberNames + " 发言");//无用
			}
			
			//这里把所有的协同播放器全部返回，不管是不是本次业务的
			List<BusinessPlayerVO> splits = new ArrayList<BusinessPlayerVO>();
			for(CommandGroupUserPlayerPO player : member.getPlayers()){
				if(PlayerBusinessType.COOPERATE_COMMAND.equals(player.getPlayerBusinessType())){
					BusinessPlayerVO split = new BusinessPlayerVO().set(player);
					splits.add(split);
				}
			}
			message.put("splits", splits);
			
			messageCaches.add(new MessageSendCacheBO(member.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND));
		}		
		
		//本段应该没用了，暂时保留不动
		//处理拒绝用户，释放其他成员的播放器，删除转发PO：查找该成员为源的转发，状态是UNDONE的，查找目的播放器；给主席发消息
		List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		for(CommandGroupMemberPO refuseMember : refuseMembers){
			refuseMember.setCooperateStatus(MemberStatus.DISCONNECT);
			List<Long> srcMemberIds = new ArrayListWrapper<Long>().add(refuseMember.getId()).getList();
			Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsBySrcmemberIds(forwards, srcMemberIds, ForwardBusinessType.COOPERATE_COMMAND, null);
			
			for(CommandGroupForwardPO forward : relativeForwards){
				forward.setGroup(null);
				if(forward.getExecuteStatus().equals(ExecuteStatus.UNDONE)
						|| forward.getExecuteStatus().equals(ExecuteStatus.DONE)){//UNDONE表示找到了播放器，此时应该不会出现DONE
					CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
					if(OriginType.OUTER.equals(dstMember.getOriginType())){
						continue;
					}
					for(CommandGroupUserPlayerPO player : dstMember.getPlayers()){
						if(player.getBundleId().equals(forward.getDstVideoBundleId()) 
								&& player.getPlayerBusinessType().equals(PlayerBusinessType.COOPERATE_COMMAND)){
							player.setFree();
							needFreePlayers.add(player);
							dstMember.getPlayers().remove(player);
						}
					}
				}
			}
			forwards.removeAll(relativeForwards);
			
			if(!OriginType.OUTER.equals(chairman.getOriginType())){
				JSONObject message = new JSONObject();
				message.put("businessType", "cooperationRefuse");
				//考虑businessId中添加其他信息
				message.put("businessId", group.getId().toString());
				if(groupType.equals(GroupType.BASIC)){
					message.put("businessInfo", refuseMember.getUserName() + " 拒绝与您协同" + commandString);
				}else if(groupType.equals(GroupType.MEETING)){
					message.put("businessInfo", refuseMember.getUserName() + " 拒绝发言");//无用
				}
				messageCaches.add(new MessageSendCacheBO(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName()));
			}
		}
		
		for(CommandGroupForwardPO forward : forwardsReadyAndCanBeDone){
			forward.setExecuteStatus(ExecuteStatus.DONE);
		}
		
		commandGroupDao.save(group);
//		commandGroupUserPlayerDao.save(needFreePlayers);
		
		//生成connectBundle，携带转发信息
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = commandBasicServiceImpl.openBundle(null, null, null, forwardsReadyAndCanBeDone, null, codec, chairman.getUserNum());
		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, forwardsReadyAndCanBeDone, null, null, null, codec, group.getUserId());
		logic.merge(logicCastDevice);		
		
		executeBusiness.execute(logic, group.getName() + " 协同会议成员接听和拒绝");
		
		//发消息
		for(MessageSendCacheBO cache : messageCaches){
			WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
			
	}
}
