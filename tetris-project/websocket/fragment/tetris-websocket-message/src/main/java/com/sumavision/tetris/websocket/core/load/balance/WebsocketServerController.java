package com.sumavision.tetris.websocket.core.load.balance;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/websocket/server")
public class WebsocketServerController {

	@Autowired
	private WebsocketServerQuery websocketServerQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addr")
	public Object addr(HttpServletRequest request) throws Exception{
		return websocketServerQuery.addr();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tourist/addr")
	public Object touristAddr(HttpServletRequest request) throws Exception{
		return websocketServerQuery.touristAddr();
	}
	
}
