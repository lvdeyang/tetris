package com.sumavision.tetris.websocket.message;

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
	
}
