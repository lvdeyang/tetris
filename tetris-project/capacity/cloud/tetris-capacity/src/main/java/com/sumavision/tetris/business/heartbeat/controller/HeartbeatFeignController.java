package com.sumavision.tetris.business.heartbeat.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.business.heartbeat.service.HeartbeatService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/capacity/heartbeat/feign")
public class HeartbeatFeignController {
	
	@Autowired
	private HeartbeatService heartbeatService;

	/**
	 * 设置心跳地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 下午1:19:12
	 * @param String capacityIp 能力ip
	 * @param String heartbeatUrl 心跳地址
	 */ 
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/heartbeatUrl")
	public Object setAlarmUrl(
			String capacityIp,
			String heartbeatUrl,
			HttpServletRequest request) throws Exception{
		
		heartbeatService.setHeartbeatUrl(capacityIp, heartbeatUrl);
		
		return null;
	}
	
}
