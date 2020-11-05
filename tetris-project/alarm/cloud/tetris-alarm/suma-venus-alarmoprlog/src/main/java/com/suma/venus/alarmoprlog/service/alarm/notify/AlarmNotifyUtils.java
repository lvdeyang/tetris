package com.suma.venus.alarmoprlog.service.alarm.notify;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyMethod;
import com.suma.venus.alarmoprlog.websocket.WebSocketServer;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;

public class AlarmNotifyUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(AlarmNotifyUtils.class);

	public static boolean startNotify(AlarmPO alarmPO, SubscribeAlarmPO subscribeAlarmPO, IAlarmDAO alarmDAO,
			RestTemplate restTemplate, RestTemplate loadBalancedRestTemplate) {

		alarmPO.setLastNotifyTime(new Date());
		alarmDAO.save(alarmPO);

		if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.HTTP)) {

			return HttpRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getCallbackUrl(), restTemplate);

		} else if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.RIBBON_LOADBALANCED)) {

			return RibbonRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getSubServiceName(),
					subscribeAlarmPO.getCallbackUrl(), loadBalancedRestTemplate);

		} else if (subscribeAlarmPO.getAlarmNotifyMethod().equals(EAlarmNotifyMethod.MESSAGE_SERVICE)) {

			// TODO 不该走到这里
			return true;

		} else {
			// 默认走普通HTTP
			return HttpRestTemplateNotify(transFromPO(alarmPO), subscribeAlarmPO.getCallbackUrl(), restTemplate);

		}

	}

	public static AlarmNotifyBO transFromPO(AlarmPO alarmPO) {

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
			if (alarmPO.getAlarmStatus().equals(EAlarmStatus.AUTO_RECOVER)
					|| alarmPO.getAlarmStatus().equals(EAlarmStatus.MANUAL_RECOVER)) {
				alarmNotifyBO.setRecoverTime(alarmPO.getRecoverTime());
			}
			return alarmNotifyBO;
		}

		return null;
	}

	private static boolean HttpRestTemplateNotify(Object obj, String callbackUrl, RestTemplate restTemplate) {
		try {

			String str = restTemplate.postForObject(callbackUrl, obj, String.class);
			return true;

		} catch (Exception e) {
			LOGGER.error("RibbonRestTemplateNotify exception, e=" + e.getMessage(), e);
			e.printStackTrace();
		}

		return false;

	}

	private static boolean RibbonRestTemplateNotify(Object obj, String serviceName, String callbackUrl,
			RestTemplate loadBalancedRestTemplate) {

		try {
			LOGGER.info("RibbonRestTemplateNotify start, url =" + "http://" + serviceName + callbackUrl + " ,params="
					+ JSONObject.toJSONString(obj));

			loadBalancedRestTemplate.postForObject("http://" + serviceName + callbackUrl, obj, String.class);
			return true;

			// 添加token头-header
			// HttpHeaders headers = new HttpHeaders();

			// headers.add(HttpConstant.HEADER_FEIGN_CLIENT,
			// HttpConstant.HEADER_FEIGN_CLIENT_KEY);
			// MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			// HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,
			// headers);
			// String result = restTemplate.postForObject("http://" + serviceName +
			// callbackUrl, httpEntity, String.class);

		} catch (Exception e) {
			LOGGER.error("RibbonRestTemplateNotify exception, e=" + e.getMessage(), e);
			e.printStackTrace();
		}

		return false;
	}

	public static void pushWebSoketAlarm(AlarmPO alarmPO) {

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

}
