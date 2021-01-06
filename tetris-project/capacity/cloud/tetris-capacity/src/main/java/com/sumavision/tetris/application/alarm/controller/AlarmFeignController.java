package com.sumavision.tetris.application.alarm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.application.alarm.service.AlarmService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 转换模块发来的告警
 */
@Controller
@RequestMapping(value = "/capacity/alarm/feign")
public class AlarmFeignController {
	
	@Autowired
	private AlarmService alarmService;

	/**
	 * 设置能力告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午2:56:20
	 * @param String capacityIp 能力ip
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/alarmUrl")
	public Object setAlarmUrl(
			String capacityIp,
			HttpServletRequest request) throws Exception{
		
		alarmService.setAlarmUrl(capacityIp);
		
		return null;
	}
	
}
