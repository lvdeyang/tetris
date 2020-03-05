package com.sumavision.tetris.websocket.message;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/message")
public class WebsocketMessageController {

	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private WebsocketMessageDAO websocketMessageDao;
	
	@Autowired
	private WebsocketMessageQuery websocketMessageQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 只推送消息无数据持久化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午9:29:29
	 * @param String targetId 目标id
	 * @param String businessId 业务id
	 * @param String content 业务内容
	 * @param String fromId 消息发布者id
	 * @param String fromName 消息发布者名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/push")
	public Object push(
			String targetId,
			String businessId,
			String content,
			String fromId,
			String fromName,
			HttpServletRequest request) throws Exception{
		
		websocketMessageService.push(targetId, businessId, JSON.parseObject(content), fromId, fromName);
		return null;
	} 
	
	/**
	 * 发送消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:35:30
	 * @param Long userId 用户id
	 * @param String message 推送消息
	 * @param String type 消息类型
	 * @param Long fromUserId 消息来源用户id
	 * @param String fromUsername 消息来源用户名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/send")
	public Object send(
			Long userId,
			String message,
			String type,
			Long fromUserId,
			String fromUsername,
			HttpServletRequest request) throws Exception{
		if(fromUserId == null){
			UserVO user = userQuery.current();
			fromUserId = user.getId();
		}
		return websocketMessageService.send(userId, message, WebsocketMessageType.valueOf(type), fromUserId, fromUsername);
	}
	
	/**
	 * 重发消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午4:04:43
	 * @param Long id 消息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resend")
	public Object resend(Long id, HttpServletRequest request) throws Exception{
		websocketMessageService.resend(id);
		return null;
	}
	
	/**
	 * 批量消费临时消息,消费后不会再向前端推消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 下午4:16:56
	 * @param JSONArray ids 消息id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/consume/all")
	public Object consumeAll(
			String ids, 
			HttpServletRequest request) throws Exception{
		List<Long> parsedIds = JSON.parseArray(ids, Long.class);
		websocketMessageService.consumeAll(parsedIds);
		return null;
	}
	
	/**
	 * 消费一个消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午5:00:24
	 * @param Long id 消息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/consume")
	public Object consume(
			Long id, 
			HttpServletRequest request) throws Exception{
		websocketMessageService.consume(id);
		return null;
	}
	
	/**
	 * 根据发送用户查询离线消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:08:25
	 * @param Long fromUserId 发送用户id
	 * @return List<WebsocketMessageVO> 消息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/unconsumed/instant/message/by/from/user/id")
	public Object findUnconsumedInstantMessageByFromUserId(
			Long fromUserId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<WebsocketMessagePO> entities = websocketMessageDao.findByUserIdAndFromUserIdAndMessageTypeAndConsumed(user.getId(), fromUserId, WebsocketMessageType.INSTANT_MESSAGE, false);
		List<WebsocketMessageVO> messages = new ArrayList<WebsocketMessageVO>();
		if(entities!=null && entities.size()>0){
			for(WebsocketMessagePO entity:entities){
				messages.add(new WebsocketMessageVO().set(entity));
			}
		}
		return messages;
	}
	/**
	 * 分用户统计当前用户的离线消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午4:05:47
	 * @return List<StatisticsInstantMessageResultVO> 统计结果
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/statistics/unconsumed/instant/message/munber")
	public Object statisticsUnconsumedInstantMessageNumber(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<StatisticsInstantMessageResultDTO> entities = websocketMessageDao.statisticsUnconsumedInstantMessageNumber(user.getId());
		List<StatisticsInstantMessageResultVO> messages = new ArrayList<StatisticsInstantMessageResultVO>();
		if(entities!=null && entities.size()>0){
			for(StatisticsInstantMessageResultDTO entity:entities){
				messages.add(new StatisticsInstantMessageResultVO().set(entity));
			}
		}
		return messages;
	}
	
	/**
	 * 查询当前用户未消费的命令消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:41:56
	 * @return List<WebsocketMessageVO> 命令列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/unconsumed/commands")
	public Object findUnconsumedCommands(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<WebsocketMessagePO> entities = websocketMessageDao.findByUserIdAndMessageTypeAndConsumed(user.getId(), WebsocketMessageType.COMMAND, false);
		List<WebsocketMessageVO> messages = new ArrayList<WebsocketMessageVO>();
		if(entities!=null && entities.size()>0){
			for(WebsocketMessagePO entity:entities){
				messages.add(new WebsocketMessageVO().set(entity));
			}
		}
		return messages;
	}
	
	/**
	 * 查询当前用户未消费的命令数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:41:56
	 * @return Long 命令数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/count/by/unconsumed/commands")
	public Object countByUnconsumedCommands(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return websocketMessageDao.countByUserIdAndMessageTypeAndConsumed(user.getId(), WebsocketMessageType.COMMAND, false);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/instant/message")
	public void broadcastMeetingMessage(
			Long commandId,
			String userIds,
			String message,
			Long fromUserId,
			String fromUsername) throws Exception{
		List<Long> parsedIds = JSON.parseArray(userIds, Long.class);
		websocketMessageService.broadcastMeetingMessage(commandId, parsedIds, message, fromUserId, fromUsername);
	}
	
	/**
	 * 查询历史及时消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午10:20:27
	 * @param Long commandId 会议id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<WebsocketMessageVO> rows 消息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/history/instant/message")
	public Object queryHistoryInstantMessage(
			Long commandId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		long total = websocketMessageDao.countByUserIdAndMessageType(commandId, WebsocketMessageType.INSTANT_MESSAGE);
		List<WebsocketMessageVO> rows = websocketMessageQuery.findByUserIdAndMessageTypeOrderByUpdateTimeDesc(commandId, WebsocketMessageType.INSTANT_MESSAGE, currentPage, pageSize);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
