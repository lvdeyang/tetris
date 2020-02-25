package com.sumavision.tetris.websocket.message;

import java.util.Collection;

import org.springframework.cloud.netflix.feign.FeignClient;
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
			@RequestParam("message") String message,
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
	
}
