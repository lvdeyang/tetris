package com.sumavision.tetris.alarm.clientservice.http;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.alarm.bo.http.OprlogParamBO;
import com.sumavision.tetris.alarm.bo.http.SubscribeParamBO;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-alarm", configuration = FeignConfiguration.class)
public interface AlarmFeign {

	@RequestMapping(value = "/alarm/tiggerAlarm", method = RequestMethod.POST)
	public JSONObject triggerAlarm(@RequestBody AlarmParamBO alarmParamBO) throws Exception;

	@RequestMapping(value = "/alarm/recoverAlarm", method = RequestMethod.POST)
	public JSONObject recoverAlarm(@RequestBody AlarmParamBO alarmParamBO) throws Exception;

	@RequestMapping(value = "/subscribe/subscribeAlarm", method = RequestMethod.POST)
	public JSONObject subscribeAlarm(@RequestBody SubscribeParamBO subscribeParamBO) throws Exception;

	@RequestMapping(value = "/subscribe/unSubscribeAlarm", method = RequestMethod.POST)
	public JSONObject unSubscribeAlarm(@RequestBody SubscribeParamBO subscribeParamBO) throws Exception;
	
	@RequestMapping(value = "/oprlog/triggerOprlog", method = RequestMethod.POST)
	public JSONObject sendOprlog(@RequestBody OprlogParamBO oprlogParamBO) throws Exception;
	

}
