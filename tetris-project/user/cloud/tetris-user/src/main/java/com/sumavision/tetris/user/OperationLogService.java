package com.sumavision.tetris.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeign;

@Component
public class OperationLogService {
	
	@Autowired
	private AlarmFeign alarmFeign;
	
	
	/**
	 * 用戶日志<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月16日 下午4:31:23
	 * @param username 操作用戶
	 * @param operationName 操作名稱
	 * @param detail 操作詳情
	 * @param type 操作類型
	 */
	public void send(
			String username, 
			String operationName, 
			String detail,
			EOprlogType type){
		OprlogParamBO log = new OprlogParamBO();
		log.setSourceService("tetris-user");
		log.setUserName(username);
		log.setOprName(operationName);
		log.setSourceServiceIP("");
		log.setOprDetail(detail);
		log.setOprlogType(type);
		try{
			alarmFeign.sendOprlog(log);
		}catch(Exception e){
			System.out.println("日志存储失败！");
			System.out.println(JSON.toJSONString(log));
		}
	}

}
