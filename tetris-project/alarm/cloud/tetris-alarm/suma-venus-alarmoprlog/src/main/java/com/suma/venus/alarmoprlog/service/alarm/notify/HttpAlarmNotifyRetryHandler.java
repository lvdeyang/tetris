package com.suma.venus.alarmoprlog.service.alarm.notify;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.InitListener;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.service.alarm.vo.AlarmRetryNotifyVO;

/**
 * 
 * 告警通知重试
 * 
 * @author chenmo
 *
 */
@Component
public class HttpAlarmNotifyRetryHandler implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);

	private static LinkedList<AlarmRetryNotifyVO> retryNotifyList = new LinkedList<AlarmRetryNotifyVO>();

	private static LinkedList<AlarmRetryNotifyVO> sendList;

	private static int MAX_RETRY_NUM = 100;

	@Autowired
	@LoadBalanced
	private RestTemplate loadBalancedRestTemplate;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IAlarmDAO alarmDAO;

	@Override
	public void run() {

		LOGGER.debug("HttpAlarmNotifyRetryHandler, start");

		sendList = new LinkedList<AlarmRetryNotifyVO>(retryNotifyList);
		retryNotifyList = new LinkedList<AlarmRetryNotifyVO>();

		try {
			while (true) {
				if (!sendList.isEmpty()) {
					LOGGER.info("alarmNotigyRetryList is not empty, start retry");
					AlarmRetryNotifyVO alarmRetryNotifyVO = sendList.poll();

					if (alarmRetryNotifyVO.getRetryNum() >= MAX_RETRY_NUM) {
						// 超过最大重试次数，丢弃
						LOGGER.warn(
								"alarm retry time heats limit, alarm=" + JSONObject.toJSONString(alarmRetryNotifyVO));
						continue;
					}

					HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(
							alarmRetryNotifyVO.getAlarmPO(), alarmRetryNotifyVO.getSubscribeAlarmPO(), null, alarmDAO,
							loadBalancedRestTemplate, restTemplate, true);

					// 放入线程池待执行
					AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

				} else {
					LOGGER.debug("sendList is empty, sleep");
					Thread.sleep(5 * 1000);
					break;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public synchronized static void putAlarmRetryNotifyVO(AlarmRetryNotifyVO vo) {
		retryNotifyList.add(vo);

	}

}
