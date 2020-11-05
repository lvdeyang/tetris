package com.sumavision.bvc.device.group.service.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import com.suma.venus.alarmoprlog.client.AlarmOprlogClientService;

@Service
public class LogService {
	
//	private  AlarmOprlogClientService alarmOprlogClientService; 
	
	private static final Logger LOG = LoggerFactory.getLogger(LogService.class);

	/**
	 * @Title: 操作日志处理 <br/>
	 * @param userName 用户名称
	 * @param operationType 操作名称
	 * @param message 备注信息
	 * @throws Exception
	 * @throws
	 */
	public void logsHandle(String userName, String operationType, String message) throws Exception{
		
//		if(null == alarmOprlogClientService){
//			alarmOprlogClientService = AlarmOprlogClientService.getInstance();
//		}
//		if(null == alarmOprlogClientService){
//			return;
//		}
		
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LOG.info("用户：" + userName + "，时间：" + time.format(new Date()) +
				"，操作类型：" + operationType + "，备注信息：" + message);
//		alarmOprlogClientService.sendOprLog(userName, operationType, message, Calendar.getInstance().getTime());
	}
}
