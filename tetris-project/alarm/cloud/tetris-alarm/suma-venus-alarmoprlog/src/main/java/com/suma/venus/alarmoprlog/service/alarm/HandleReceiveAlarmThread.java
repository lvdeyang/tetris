package com.suma.venus.alarmoprlog.service.alarm;

import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.alarm.bo.AlarmParamBO;

/**
 * 
 * 告警消息排队、分发线程
 * 
 * @author chenmo
 *
 */
@Component
public class HandleReceiveAlarmThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(HandleReceiveAlarmThread.class);

	@Autowired
	private AlarmHandler alarmHandler;

	// 缓冲队列
	private static LinkedBlockingDeque<AlarmParamBO> alarmQueue = new LinkedBlockingDeque<>(20480);

	// 将操作日志压入缓存
	public static void push(AlarmParamBO alarmParamBO) {
		try {
			alarmQueue.putLast(alarmParamBO);
		} catch (InterruptedException e) {
			LOGGER.error("Cannot put one AlarmBO in Queue", e);
		}
	}

	@Override
	public void run() {

		LOGGER.info("HandleReceiveAlarmThread start");
		while (true) {
			try {
				AlarmParamBO alarmParamBO = alarmQueue.takeFirst();
				handleMsg(alarmParamBO);
			} catch (InterruptedException e) {
				// LOGGER.error(e.toString());
			}
		}
	}

	private void handleMsg(AlarmParamBO alarmParamBO) {

		try {

			alarmHandler.handleAlarm(alarmParamBO);

		} catch (Exception e) {
			System.out.println(e.toString());
			LOGGER.error("error: " + e);
		}
	}

}
