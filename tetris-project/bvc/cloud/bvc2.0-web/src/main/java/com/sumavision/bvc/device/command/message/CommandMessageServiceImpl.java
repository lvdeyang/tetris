package com.sumavision.bvc.device.command.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMessageDAO;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.enumeration.MessageStatus;
import com.sumavision.bvc.command.group.message.CommandGroupMessagePO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandMessageServiceImpl 
* @Description: 指挥消息
* @author zsy
* @date 2019年11月19日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandMessageServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupMessageDAO commandGroupMessageDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 新建通知并发送<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:25:23
	 * @param groupId
	 * @param userId
	 * @param content
	 * @param forcedRolling
	 * @param style
	 * @param dstUserIds
	 * @return
	 * @throws Exception
	 */
	public CommandGroupMessagePO sendMessage(
			Long groupId,
			Long userId,
			String content,
			boolean forcedRolling,
			String style,
		    List<Long> dstUserIds) throws Exception{
		
		//userIdsStr用“-”分隔
		String userIdsStr = StringUtils.join(dstUserIds.toArray(), "-");
		CommandGroupMessagePO messagePO = new CommandGroupMessagePO(
				groupId,
				userIdsStr,
				MessageStatus.START,
				content,
				forcedRolling,
				style);
		
		commandGroupMessageDao.save(messagePO);
		
		//发消息		
		sendMessageUtil(messagePO);
		
		return messagePO;
	}
	
	/**
	 * 再次通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:24:46
	 * @param messageId
	 * @throws Exception
	 */
	public void resend(Long messageId) throws Exception{
		
		CommandGroupMessagePO messagePO = commandGroupMessageDao.findOne(messageId);
		messagePO.setStatus(MessageStatus.START);
		commandGroupMessageDao.save(messagePO);
		sendMessageUtil(messagePO);
	}
	
	/**
	 * 发送通知消息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:25:01
	 * @param messagePO
	 * @throws Exception
	 */
	private void sendMessageUtil(CommandGroupMessagePO messagePO) throws Exception{
		
		Long groupId = messagePO.getGroupId();
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		List<Long> consumeIds = new ArrayList<Long>();
		JSONObject message = new JSONObject();
		message.put("businessType", "commandMessageReceive");
		message.put("businessId", groupId + "-" + messagePO.getId());
		message.put("date", DateUtil.format(messagePO.getUpdateTime(), DateUtil.dateTimePattern));
		message.put("businessInfo", group.getName() + " 给你转发了消息");
		message.put("content", messagePO.getContent());
		message.put("forcedRolling", messagePO.isForcedRolling());
		message.put("style", JSON.parse(messagePO.getStyle()));
		
		List<String> dstUserIdsStr = Arrays.asList(messagePO.getUserIds().split("-"));
		for(String dstUserIdStr : dstUserIdsStr){
			WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(dstUserIdStr), message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	/**
	 * 停止一个通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:24:21
	 * @param messageId
	 * @throws Exception
	 */
	public void stop(Long messageId) throws Exception{
		
		CommandGroupMessagePO messagePO = commandGroupMessageDao.findOne(messageId);
		messagePO.setStatus(MessageStatus.STOP);
		commandGroupMessageDao.save(messagePO);
		Long groupId = messagePO.getGroupId();
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		List<Long> consumeIds = new ArrayList<Long>();
		
		JSONObject message = new JSONObject();
		message.put("businessType", "commandMessageStop");
		message.put("businessId", groupId + "-" + messageId);
		message.put("businessInfo", group.getName() + " 停止了转发消息");		
		
		List<String> dstUserIdsStr = Arrays.asList(messagePO.getUserIds().split("-"));
		for(String dstUserIdStr : dstUserIdsStr){
			WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(dstUserIdStr), message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
			consumeIds.add(ws.getId());
		}
		websocketMessageService.consumeAll(consumeIds);
	}
	
	/**
	 * 停止一个指挥中的全部通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:24:03
	 * @param groupId
	 * @throws Exception
	 */
	public void stopAll(Long groupId) throws Exception{
		
		List<CommandGroupMessagePO> messagePOs = commandGroupMessageDao.findByGroupId(groupId);
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		List<Long> consumeIds = new ArrayList<Long>();
		
		for(CommandGroupMessagePO messagePO : messagePOs){
			if(messagePO.getStatus().equals(MessageStatus.STOP)){
				continue;
			}
			messagePO.setStatus(MessageStatus.STOP);
			JSONObject message = new JSONObject();
			message.put("businessType", "commandMessageStop");
			message.put("businessId", groupId + "-" + messagePO.getId());
			message.put("businessInfo", group.getName() + " 停止了转发消息");		
			
			List<String> dstUserIdsStr = Arrays.asList(messagePO.getUserIds().split("-"));//JSONArray.parseArray(messagePO.getUserIds(), Long.class);
			for(String dstUserIdStr : dstUserIdsStr){
				WebsocketMessageVO ws = websocketMessageService.send(Long.parseLong(dstUserIdStr), message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
				consumeIds.add(ws.getId());
			}
		}
		commandGroupMessageDao.save(messagePOs);
		websocketMessageService.consumeAll(consumeIds);
	}
	
	/**
	 * 删除多个通知<br/>
	 * <p>如有通知未停止，则抛错</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:22:31
	 * @param messageIds
	 * @throws Exception 如有通知未停止，则抛错
	 */
	public void remove(List<Long> messageIds) throws Exception{
		
		//校验有没有停止
		List<CommandGroupMessagePO> messages = commandGroupMessageDao.findAll(messageIds);
		for(CommandGroupMessagePO message : messages){
			if(!message.getStatus().equals(MessageStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, message.getContent() + " 通知未停止，无法删除，id: " + message.getId());
			}
		}
		commandGroupMessageDao.deleteByIdIn(messageIds);
	}

	/**
	 * 发送实时消息，给会议内的成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 下午7:06:59
	 * @param userId 发起人id
	 * @param name 发起人名字
	 * @param groupId 指挥id
	 * @param message 消息
	 * @throws Exception
	 */
	public void broadcastInstantMessage(Long userId, String name, Long groupId, String message) throws Exception{
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		List<CommandGroupMemberPO> members = group.getMembers();
		List<Long> userIds = new ArrayList<Long>();
		for(CommandGroupMemberPO member : members){
			if(member.getMemberStatus().equals(MemberStatus.CONNECT) && !member.getUserId().equals(userId)){
				userIds.add(member.getUserId());
			}
		}
		websocketMessageService.broadcastMeetingMessage(groupId, userIds, message, userId, name);
		
	}
}
