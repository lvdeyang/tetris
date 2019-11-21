package com.sumavision.tetris.p2p.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 用户websocket断开事件发布代理<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月23日 下午1:16:46
 */
@FeignClient(name = "tetris-p2p", configuration = FeignConfiguration.class)
public interface WebsocketSessionClosedFeign {

	/**
	 * 用户websocket断开事件发布代理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 上午11:15:01
	 * @param Long userId 用户id
	 */
	@RequestMapping(value = "/event/publish/websocket/closed")
	public void websocketClosed(@RequestParam("userId") Long userId) throws Exception;
}
