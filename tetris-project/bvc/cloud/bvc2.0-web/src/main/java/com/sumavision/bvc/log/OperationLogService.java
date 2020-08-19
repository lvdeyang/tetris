package com.sumavision.bvc.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeign;

@Component
public class OperationLogService {

	@Autowired
	private AlarmFeign alarmFeign;
	
	/**
	 * 发送日志<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午5:31:36
	 * @param String username 操作用户
	 * @param String operationName 操作名称
	 * @param String detail 详情
	 */
	public void send(
			String username, 
			String operationName, 
			String detail){
		OprlogParamBO log = new OprlogParamBO();
		log.setSourceService("tetris-bvc-business");
		log.setUserName(username);
		log.setOprName(operationName);
		log.setSourceServiceIP("");
		log.setOprDetail(detail);
		try{
			alarmFeign.sendOprlog(log);
		}catch(Exception e){
			System.out.println("日志存储失败！");
			System.out.println(JSON.toJSONString(log));
		}
	}
	
}
