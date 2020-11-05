package com.sumavision.tetris.business.alarm.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.business.api.vo.AlarmVO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.capacity.bo.request.ResultCodeResponse;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.config.ServerProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AlarmService {

	private static final Logger LOG = LoggerFactory.getLogger(AlarmService.class);


	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private CapacityProps capacityProps;
	
	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private SyncService syncService;
	
	@Autowired
	private AlarmFeignClientService alarmFeignClientService;

	/**
	 * 设置能力告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午2:50:30
	 * @param String ip 能力ip
	 */
	public void setAlarmUrl(String ip) throws Exception{
		
		String eurake = serverProps.getDefaultZone().split("http://")[1].split(":")[0];
		
		String alarmUrl = new StringBufferWrapper().append("http://")
												   .append(eurake)
												   .append(":")
												   .append(8082)
												   .append("/tetris-capacity/api/thirdpart/capacity/alarm/notify?bundle_ip=")
												   .append(ip)
												   .toString();
		ResultCodeResponse response = capacityService.putAlarmUrl(ip, capacityProps.getPort(), alarmUrl);
		if(response.getResult_code().equals("1")){
			throw new BaseException(StatusCode.ERROR, "url格式错误");
		}
	}
	
	/**
	 * 告警通知<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午10:39:07
	 * @param String capacityIp 能力ip
	 * @param AlarmVO alarm 告警参数
	 */
	public void alarmNotify(String capacityIp, AlarmVO alarm) throws Exception{
		
		String alarmCode = alarm.getCodec();
		
		try {
			if("11070001".equals(alarmCode)){
				LOG.info("transform online");
				syncService.sync(capacityIp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject alarmObj = new JSONObject();
		if(alarm.getInput_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getInput_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getTask_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getTask_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getOutput_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getOutput_trigger()));
			alarmObj.put("detail",alarm.getDetail());
		}
		if(alarm.getLicense_trigger() != null){
			alarmObj = JSONObject.parseObject(JSONObject.toJSONString(alarm.getLicense_trigger()));
			alarmObj.put("details",alarm.getDetail());
		}
		
		try{
			if(alarm.getStatus().equals("on")){
				alarmFeignClientService.triggerAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, false, new Date());
			}
			if(alarm.getStatus().equals("off")){
				alarmFeignClientService.recoverAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, new Date());
			}
			if(alarm.getStatus().equals("once")){
				alarmFeignClientService.triggerAlarm(alarmCode, capacityIp, alarmObj.toJSONString(), null, true, new Date());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
