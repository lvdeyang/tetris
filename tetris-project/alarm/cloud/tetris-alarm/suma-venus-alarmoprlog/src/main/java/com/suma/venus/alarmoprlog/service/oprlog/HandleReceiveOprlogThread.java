package com.suma.venus.alarmoprlog.service.oprlog;

import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.alarmoprlog.orm.dao.IOprlogDAO;
import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;

@Component
public class HandleReceiveOprlogThread extends Thread {

	@Autowired
	private IOprlogDAO oprlogDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(HandleReceiveOprlogThread.class);

	// 缓冲队列
	private static LinkedBlockingDeque<OprlogParamBO> oprlogQueue = new LinkedBlockingDeque<>(10240);

	// 将操作日志压入缓存
	public static void push(OprlogParamBO OprlogParamBO) {
		try {
			oprlogQueue.putLast(OprlogParamBO);
		} catch (InterruptedException e) {
			LOGGER.error("Cannot put one AlarmBO in Queue", e);
		}
	}

	@Override
	public void run() {

		LOGGER.info("HandleReceiveAlarmThread start");
		while (true) {
			try {
				OprlogParamBO OprlogParamBO = oprlogQueue.takeFirst();
				handleMsg(OprlogParamBO);
			} catch (InterruptedException e) {
				// LOGGER.error(e.toString());
			}
		}
	}

	private void handleMsg(OprlogParamBO oprlogParamBO) {
		try {
			OprlogPO oprlogPO = new OprlogPO();

			BeanUtils.copyProperties(oprlogParamBO, oprlogPO);
			
			

			oprlogDAO.save(oprlogPO);

		} catch (Exception e) {
			System.out.println(e.toString());
			LOGGER.error("error: " + e);
		}
	}

}
