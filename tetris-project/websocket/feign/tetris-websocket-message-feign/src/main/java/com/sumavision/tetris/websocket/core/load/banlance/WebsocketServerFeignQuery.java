package com.sumavision.tetris.websocket.core.load.banlance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class WebsocketServerFeignQuery {

	@Autowired
	private WebsocketServerFeign websocketServerFeign;
	
	/**
	 * 登陆用户websocket连接地址 <br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月2日 下午2:43:58
	 * @return
	 */
	public String addr()throws Exception{
		return JsonBodyResponseParser.parseObject(websocketServerFeign.addr(), String.class);
	}
}
