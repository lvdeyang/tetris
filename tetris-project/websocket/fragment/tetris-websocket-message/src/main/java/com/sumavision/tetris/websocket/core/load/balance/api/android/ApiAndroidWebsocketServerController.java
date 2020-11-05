package com.sumavision.tetris.websocket.core.load.balance.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.websocket.core.load.balance.WebsocketServerQuery;

@Controller
@RequestMapping(value = "/api/android/websocket/server")
public class ApiAndroidWebsocketServerController {

	@Autowired
	private WebsocketServerQuery websocketServerQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addr")
	public Object addr(HttpServletRequest request) throws Exception{
		return websocketServerQuery.addr();
	}
	
}
