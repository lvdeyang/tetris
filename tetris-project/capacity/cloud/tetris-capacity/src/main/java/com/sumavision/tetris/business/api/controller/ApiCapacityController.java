package com.sumavision.tetris.business.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.alarm.service.AlarmService;
import com.sumavision.tetris.business.api.vo.AlarmVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/api/thirdpart/capacity")
public class ApiCapacityController {
	
	@Autowired
	private AlarmService alarmService;
	
	/**
	 * 能力告警通知<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:48:59
	 * @param JSONObject alarmInfo 告警信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/alarm/notify")
	public Object alarmNotify(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String remoteIp = request.getParameter("bundle_ip");
		
		AlarmVO alarm = JSONObject.parseObject(requestWrapper.getString("alarm"), AlarmVO.class);
		
		alarmService.alarmNotify(remoteIp, alarm);
		
		return new HashMapWrapper<String, Object>().put("msg_id", requestWrapper.getString("msg_id")).getMap();
	}
	
}
