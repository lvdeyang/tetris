package com.sumavision.tetris.websocket.core.load.balance.api.zoom.android;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.websocket.core.load.balance.WebsocketServerQuery;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/api/zoom/android/websocket/server")
public class ApiZoomAndroidWebsocketServerController {

	@Autowired
	private WebsocketServerQuery websocketServerQuery;
	
	/**
	 * 获取登录用户websocket连接地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:20:19
	 * @return String 登录用户websocket连接地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addr")
	public Object addr(HttpServletRequest request) throws Exception{
		return websocketServerQuery.addr();
	}
	
	/**
	 * 获取游客websocket连接地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:21:11
	 * @return String 游客websocket连接地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tourist/addr")
	public Object touristAddr(HttpServletRequest request) throws Exception{
		return websocketServerQuery.touristAddr();
	}
	
}
