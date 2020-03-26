package com.sumavision.bvc.device.command.meeting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
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
			
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 会议已停止，无法操作，id: " + group.getId());
			}
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要申请发言");
			}
			
			//协同成员，校验是否已经被授权协同
			Set<CommandGroupMemberPO> members = group.getMembers();
			Set<CommandGroupMemberPO> cooperateMembers = new HashSet<CommandGroupMemberPO>();
			for(CommandGroupMemberPO member : members){
				
			}
			
			
			
			group.setSpeakType(GroupSpeakType.DISCUSS);
			
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
			
			if(group.getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "主席不需要申请发言");
			}
			
			//协同成员，校验是否已经被授权协同
			Set<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO member = commandCommonUtil.queryMemberByUserId(members, userId);
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			
			
			
			
			
			
			
			Set<CommandGroupMemberPO> cooperateMembers = new HashSet<CommandGroupMemberPO>();
			
			
			
			group.setSpeakType(GroupSpeakType.DISCUSS);
			
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
	public void speakApplyDisagree(Long userId, Long groupId, List<Long> userIdArray) throws Exception{
		
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
			
		}
	}
	
	
	/**
	 * 
	 * 发起协同指挥<br/>
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
		
		JSONArray chairSplits = new JSONArray();
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
					
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 指挥已停止，无法操作，id: " + group.getId());
		}
		
		if(userIdArray.contains(group.getUserId())){
			throw new BaseException(StatusCode.FORBIDDEN, "不能选择主席进行协同指挥");
		}
		
		//协同成员，校验是否已经被授权协同
		Set<CommandGroupMemberPO> members = group.getMembers();
		Set<CommandGroupMemberPO> cooperateMembers = new HashSet<CommandGroupMemberPO>();
		for(CommandGroupMemberPO member : members){
			if(userIdArray.contains(member.getUserId())){
				if(member.getCooperateStatus().equals(MemberStatus.DISCONNECT)){
					cooperateMembers.add(member);
				}else{
					throw new BaseException(StatusCode.FORBIDDEN, member.getUserName() + " 已经被授权协同指挥");
				}
			}
		}
		
		//校验协同成员是否入会
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			if(!cooperateMember.getMemberStatus().equals(MemberStatus.CONNECT)){
				throw new BaseException(StatusCode.FORBIDDEN, cooperateMember.getUserName() + " 还未进入指挥");
			}
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		Set<CommandGroupForwardPO> forwards = new HashSet<CommandGroupForwardPO>();
		CommandGroupMemberPO chairman = commandCommonUtil.queryChairmanMember(members);
		
		//除主席外，给每个成员查找memberIdArray.size()个播放器，建立转发
		for(CommandGroupMemberPO member : members){
			
			if(member.isAdministrator()){
				continue;
			}
			String cooStr = "";
			int needPlayerCount = userIdArray.size();
			if(userIdArray.contains(member.getUserId())){
				//这是协同成员
				needPlayerCount--;
				cooStr = " 也是协同成员，";
			}
			
			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(member.getUserId(), PlayerBusinessType.COOPERATE_COMMAND, needPlayerCount, false);
			int usefulPlayersCount = players.size();
			log.info(new StringBufferWrapper().append("协同成员数为 ").append(userIdArray.size())
					.append("， ").append(member.getUserName()).append(cooStr).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
			
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
				if(usefulPlayersCount > 0){
					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
					
					player.setBusinessId(group.getId().toString());//如果需要改成c2m_forward.getId()，那么需要先save获得id
					player.setBusinessName(group.getName() + " 指挥");
					player.setPlayerBusinessType(PlayerBusinessType.COOPERATE_COMMAND);
					
					//用于返回的分屏信息
					JSONObject split = new JSONObject();
					split.put("serial", player.getLocationIndex());
					split.put("bundleId", player.getBundleId());
					split.put("bundleNo", player.getCode());
					split.put("businessType", "command");
					split.put("businessId", group.getId().toString());
					split.put("businessInfo", group.getName() + " 指挥");
					split.put("status", "start");
					chairSplits.add(split);
					
					//给转发设置目的
					c2m_forward.setDstPlayer(player);
					c2m_forward.setExecuteStatus(ExecuteStatus.UNDONE);//TODO:UNDONE不行就改成WAITING_FOR_RESPONSE
					player.setMember(member);
					usefulPlayersCount--;
				}else{
					c2m_forward.setExecuteStatus(ExecuteStatus.NO_AVAILABLE_PLAYER);
				}				
			}
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
			message.put("businessInfo", "接受到 " + group.getName() + " 指挥的主席：" + chairman.getUserName() + " 邀请进入协同指挥，是否进入？");
			WebsocketMessageVO ws = websocketMessageService.send(cooperateMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
			cooperateMember.setMessageCoId(ws.getId());
		}
		
		group.getForwards().addAll(forwards);		
		
		commandGroupDao.save(group);

		}
		
		return chairSplits;
	}
	
	/**
	 * 同意协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:48:42
	 * @param userId
	 * @param groupId
	 * @throws Exception
	 */
	public void agree(Long userId, Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 指挥已经停止。id: " + group.getId());
		}
		
		CommandGroupMemberPO acceptMember = commandCommonUtil.queryMemberByUserId(group.getMembers(), userId);
		MemberStatus cooperateStatus = acceptMember.getCooperateStatus();
		if(cooperateStatus.equals(MemberStatus.CONNECT)){
			return;
		}else if(cooperateStatus.equals(MemberStatus.DISCONNECT)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "协同指挥已结束");
		}
		
		List<CommandGroupMemberPO> acceptMembers = new ArrayListWrapper<CommandGroupMemberPO>().add(acceptMember).getList();
		
		membersResponse(group, acceptMembers, null);
		
		}
	}
	
	/**
	 * 拒绝协同指挥<br/>
	 * <p>线程不安全，调用处必须使用 command-group-{groupId} 加锁</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:48:57
	 * @param userId
	 * @param groupId
	 * @throws Exception
	 */
	public void refuse(Long userId, Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);		
		CommandGroupMemberPO refuseMember = commandCommonUtil.queryMemberByUserId(group.getMembers(), userId);
		MemberStatus cooperateStatus = refuseMember.getCooperateStatus();
		if(cooperateStatus.equals(MemberStatus.CONNECT)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + "正在进行协同指挥");
		}else if(cooperateStatus.equals(MemberStatus.DISCONNECT)){
			return;
		}
		
		List<CommandGroupMemberPO> refuseMembers = new ArrayListWrapper<CommandGroupMemberPO>().add(refuseMember).getList();
		
		membersResponse(group, null, refuseMembers);
		
		}
	}
	
	/**
	 * 撤销授权协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月4日 下午1:26:41
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public JSONArray revoke(Long userId, Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append("command-group-").append(groupId).toString().intern()) {
		
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			
			if(GroupStatus.STOP.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 指挥已经停止。id: " + group.getId());
			}
		
			JSONArray groupInfos = new JSONArray();
			Set<CommandGroupForwardPO> forwards = group.getForwards();
			Set<CommandGroupMemberPO> members = group.getMembers();
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
			message.put("businessInfo", group.getName() + " 指挥主席撤销授权 " + revokeMember.getUserName() + " 协同指挥");
			
			//给被撤销成员发消息 TODO:信息可能不足
			WebsocketMessageVO ws = websocketMessageService.send(revokeMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
			consumeIds.add(ws.getId());
			
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
					WebsocketMessageVO ws2 = websocketMessageService.send(dstMember.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, chairman.getUserId(), chairman.getUserName());
					consumeIds.add(ws2.getId());
//				}
			}
			forwards.removeAll(relativeForwards);
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(needFreePlayers);
			websocketMessageService.consumeAll(consumeIds);
			
			//关闭播放器，考虑怎么给不需要挂断的编码器计数
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.closeBundle(null, null, needClosePlayers, codec, group.getUserId());
			LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(null, null, null, needClosePlayers, codec, group.getUserId());
			logic.merge(logicCastDevice);
			executeBusiness.execute(logic, group.getName() + " 指挥中撤销" + revokeMember.getUserName() + "的协同指挥授权");
			
			return groupInfos;
		
		}
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
	private void membersResponse(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers, List<CommandGroupMemberPO> refuseMembers) throws Exception{
				
			if(null == acceptMembers) acceptMembers = new ArrayList<CommandGroupMemberPO>();
			if(null == refuseMembers) refuseMembers = new ArrayList<CommandGroupMemberPO>();
			Set<CommandGroupMemberPO> members = group.getMembers();
			Set<CommandGroupForwardPO> forwards = group.getForwards();
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
									message.put("businessInfo", group.getName() + " 指挥主席授权 " + acceptMember.getUserName() + " 协同指挥");
									message.put("splits", new JSONArray());
									//用于返回的分屏信息
									JSONObject split = new JSONObject();
									split.put("serial", player.getLocationIndex());
									split.put("bundleId", player.getBundleId());
									split.put("bundleNo", player.getCode());
									split.put("businessType", "cooperation");
									split.put("businessId", group.getId() + "-" + acceptMember.getUserId());
									split.put("businessInfo", group.getName() + " 指挥-" + acceptMember.getUserName() + "协同指挥");
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
//								message.put("businessInfo", group.getName() + " 指挥主席授权 " + acceptMember.getUserName() + " 协同指挥");
//								message.put("splits", new JSONArray());
//								//用于返回的分屏信息
//								JSONObject split = new JSONObject();
//								split.put("serial", player.getLocationIndex());
//								split.put("bundleId", player.getBundleId());
//								split.put("bundleNo", player.getCode());
//								split.put("businessType", "cooperation");
//								split.put("businessId", group.getId() + "-" + acceptMember.getId());
//								split.put("businessInfo", group.getName() + " 指挥-" + acceptMember.getUserName() + "协同指挥");
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
				message.put("businessInfo", refuseMember.getUserName() + " 拒绝与您协同指挥");
//				WebsocketMessageVO ws = websocketMessageService.send(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName());
//				consumeIds.add(ws.getId());
				messageCaches.add(new MessageSendCacheBO(chairman.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, refuseMember.getUserId(), refuseMember.getUserName()));
			}
			
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(needFreePlayers);
			
			//生成connectBundle，携带转发信息
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.openBundle(acceptMembers, null, needPlayers, allNeedForwards, null, codec, group.getUserId());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, allNeedForwards, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);		
			
			executeBusiness.execute(logic, group.getName() + " 协同指挥成员接听和拒绝");
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
	}
}
