package com.sumavision.tetris.websocket.message;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 系统用户feign接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月7日 上午10:25:16
 */
@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface WebsocketMessageFeign {

	/**
	 * 只推送消息无数据持久化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午8:56:13
	 * @param String targetId 目标id
	 * @param String businessId 业务id
	 * @param String content 业务内容
	 * @param String fromId 消息发布者id
	 * @param String fromName 消息发布者名称
	 */
	@RequestMapping(value = "/message/push")
	public JSONObject push(
			@RequestParam("targetId") String targetId,
			@RequestParam("businessId") String businessId,
			@RequestParam("content") String content,
			@RequestParam("fromId") String fromId,
			@RequestParam("fromName") String fromName);
	
	/**
	 * 发送消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:35:30
	 * @param Long userId 用户id
	 * @param String message 推送消息
	 * @param String type 消息类型
	 */
	@RequestMapping(value = "/message/send")
	public JSONObject send(
			@RequestParam("userId") Long userId,
			@RequestBody String message,
			@RequestParam("type") String type);
	
	/**
	 * 重发消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午4:04:43
	 * @param Long id 消息id
	 */
	@RequestMapping(value = "/message/resend")
	public JSONObject resend(@RequestParam("id") Long id);
	
	/**
	 * 消费一个消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午5:00:24
	 * @param Long id 消息id
	 */
	@RequestMapping(value = "/message/consume")
	public JSONObject consume(@RequestParam("id") Long id);
	
	/**
	 * 批量消费消息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午2:46:01
	 * @param ids String型
	 * @return
	 */
	@RequestMapping(value = "/message/consume/all")
	public JSONObject consumeAll(@RequestParam("ids") String ids);
	
	@RequestMapping(value = "/message/find/unconsumed/instant/message/by/from/user/id")
	public JSONObject findUnconsumedInstantMessageByFromUserId(
			@RequestParam("fromUserId") Long fromUserId);
	
	@RequestMapping(value = "/message/statistics/unconsumed/instant/message/munber")
	public JSONObject statisticsUnconsumedInstantMessageNumber();
	
	@RequestMapping(value = "/message/find/unconsumed/commands")
	public JSONObject findUnconsumedCommands();
	
	@RequestMapping(value = "/message/count/by/unconsumed/commands")
	public JSONObject countByUnconsumedCommands();
	

	@RequestMapping(value = "/message/broadcast/instant/message")
	public JSONObject broadcastMeetingMessage(
			@RequestParam("commandId") Long commandId,
			@RequestParam("userIds") String userIds,
			@RequestParam("message") String message,
			@RequestParam("fromUserId") Long fromUserId,
			@RequestParam("fromUsername") String fromUsername);
	
	@RequestMapping(value = "/message/query/history/instant/message")
	public JSONObject queryHistoryInstantMessage(
			@RequestParam("commandId") Long commandId,
			@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize")int pageSize);
	
}
