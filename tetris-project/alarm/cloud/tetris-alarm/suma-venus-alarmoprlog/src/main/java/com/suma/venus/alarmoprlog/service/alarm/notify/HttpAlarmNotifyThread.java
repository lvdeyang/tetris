package com.suma.venus.alarmoprlog.service.alarm.notify;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyMethod;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyPattern;
import com.suma.venus.alarmoprlog.websocket.WebSocketServer;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;

/**
 * 告警HTTP通知 线程
 *
 * <p>
 * 新的告警产生后，通知所有订阅此类告警的终端，请求。
 * 
 * @author 陈默 2014-3-17
 * @see
 * @since 1.0
 */
public class HttpAlarmNotifyThread implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpAlarmNotifyThread.class);

	private ISubscribeAlarmDAO subscribeAlarmDAO;

	private IAlarmDAO alarmDAO;

	private AlarmPO alarmPO;

	private RestTemplate loadBalancedRestTemplate;

	private RestTemplate restTemplate;

	public HttpAlarmNotifyThread(AlarmPO alarmPO, ISubscribeAlarmDAO subscribeAlarmDAO, IAlarmDAO alarmDAO,
			RestTemplate loadBalancedRestTemplate, RestTemplate restTemplate) {
		this.alarmPO = alarmPO;
		this.subscribeAlarmDAO = subscribeAlarmDAO;
		this.alarmDAO = alarmDAO;
		this.loadBalancedRestTemplate = loadBalancedRestTemplate;
		this.restTemplate = restTemplate;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		httpAlarmNotify(alarmPO);
	}

	/**
	 * 实时告警的http通知
	 *
	 * <p>
	 * 实时告警的http通知
	 * 
	 * @param alarmPO
	 * @see
	 * @since
	 * 
	 */
	private void httpAlarmNotify(AlarmPO alarmPO) {
		try {
			pushWebSoketMsg(alarmPO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO
					.findByAlarmInfo_AlarmCode(alarmPO.getLastAlarm().getAlarmInfo().getAlarmCode());

			if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {
				// 没有被订阅则直接返回
				return;
			}

			// 去除重复的订阅，防止重复通知
			for (int i = 0; i < subscribeAlarmPOs.size(); i++) {
				for (int j = i + 1; j < subscribeAlarmPOs.size(); j++) {
					if (subscribeAlarmPOs.get(i).getCallbackUrl().equals(subscribeAlarmPOs.get(j).getCallbackUrl())) {
						subscribeAlarmPOs.remove(j);
						j--;
					}
				}
			}

			// 告警产生的通知
			if (alarmPO.getAlarmStatus().equals(EAlarmStatus.UNTREATED)) {
				if ((alarmPO.getAlarmCount() > 1 && alarmNotifyExp(alarmPO)) || alarmPO.getAlarmCount() == 1) {
					// 已超时旧告警或者新告警通知
					for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {
						// TODO 如果收不到 怎么办
						startNotify(alarmPO, subscribeAlarmPO);
					}
				} else {
					// 每次都通知模式的告警通知
					for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {
						if (subscribeAlarmPO.getAlarmNotifyPattern().equals(EAlarmNotifyPattern.NOTIFY_EVERYTIME)) {
							startNotify(alarmPO, subscribeAlarmPO);
						}
					}
				}

				// 告警恢复等的通知
			} else {
				for (SubscribeAlarmPO subscribeAlarmPO : subscribeAlarmPOs) {
					startNotify(alarmPO, subscribeAlarmPO);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Http alarm notify failed: " + e.toString());
		}
	}

	private void pushWebSoketMsg(AlarmPO alarmPO) {

		LOGGER.info(
				"----------pushWebSoketMsg start, alarmCode==" + alarmPO.getLastAlarm().getAlarmInfo().getAlarmCode());

		// 页面通知 暂时只处理新告警
		if (alarmPO.getAlarmCount() == 1 && alarmPO.getAlarmStatus().equals(EAlarmStatus.UNTREATED)) {
			try {
				WebSocketServer.sendInfo("new", "alarm");
			} catch (IOException e) {
				LOGGER.error("pushWebSoketMsg error: " + e);
			}
		}
		LOGGER.info("----------pushWebSoketMsg finish");
	}

	private boolean RibbonRestTemplateNotify(Object obj, String serviceName, String callbackUrl) {

		try {
			loadBalancedRestTemplate.postForObject("http://" + serviceName + callbackUrl, obj, String.class);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean HttpRestTemplateNotify(Object obj, String callbackUrl) {

		String str = restTemplate.postForObject(callbackUrl, obj, String.class);
		System.out.println("test 1 str = " + str);

		return true;
	}

	/**
	 * 判断一个旧的实时告警是否超时，是否需要通知
	 *
	 * <p>
	 * 判断一个旧的实时告警是否超时，是否需要通知，超时时间暂定为10分钟
	 * 
	 * @return
	 * @see
	 * @since
	 * 
	 */
	private boolean alarmNotifyExp(AlarmPO alarmPO) {
		// 暂定超时时间为10分钟
		if (alarmPO.getLastNotifyTime() == null) {
			return true;
		}

		if (alarmPO.getLastAlarm().getCreateTime().getTime()
				- alarmPO.getLastNotifyTime().getTime() >= (10 * 60 * 1000)) {
			return true;
		}
		return false;
	}

	/**
	 * 更新alarm状态，发出http请求
	 *
	 * <p>
	 * 
	 * @param alarmPO
	 * @param subscribeAlarmPO
	 * @throws UnsupportedEncodingException
	 * @see
	 * @since
	 * 
	 */
	private boolean startNotify(AlarmPO alarmPO, SubscribeAlarmPO subscribeAlarmPO) {
		alarmPO.setLastNotifyTime(new Date());
		alarmDAO.save(alarmPO);

		if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.HTTP)) {

			return HttpRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getCallbackUrl());

		} else if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.RIBBON_LOADBALANCED)) {

			return RibbonRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getSubServiceName(),
					subscribeAlarmPO.getCallbackUrl());

		} else if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.MESSAGE_SERVICE)) {

			// TODO 不该走到这里
			return true;

		} else {
			// 默认走普通HTTP
			return HttpRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getCallbackUrl());

		}

	}

	private AlarmNotifyBO transFromPO(AlarmPO alarmPO) {

		if (alarmPO != null && alarmPO.getLastAlarm() != null) {
			AlarmNotifyBO alarmNotifyBO = new AlarmNotifyBO();
			alarmNotifyBO.setAlarmCode(alarmPO.getLastAlarm().getAlarmInfo().getAlarmCode());
			alarmNotifyBO.setAlarmTime(alarmPO.getLastAlarm().getCreateTime());
			alarmNotifyBO.setAlarmStatus(alarmPO.getAlarmStatus().toString());
			alarmNotifyBO.setSourceServiceIP(alarmPO.getLastAlarm().getSourceServiceIP());
			alarmNotifyBO.setSourceService(alarmPO.getLastAlarm().getSourceService());
			alarmNotifyBO.setAlarmDevice(alarmPO.getLastAlarm().getAlarmDevice());
			alarmNotifyBO.setAlarmObj(alarmPO.getLastAlarm().getAlarmObj());
			alarmNotifyBO.setAlarmId(alarmPO.getId());
			alarmNotifyBO.setParams(AlarmNotifyBO.formatParams(alarmPO.getLastAlarm().getAlarmParams()));
			return alarmNotifyBO;
		}

		return null;
	}

	public void setSubscribeAlarmService(ISubscribeAlarmDAO subscribeAlarmDAO) {
		this.subscribeAlarmDAO = subscribeAlarmDAO;
	}

	public void setAlarmService(IAlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

}
