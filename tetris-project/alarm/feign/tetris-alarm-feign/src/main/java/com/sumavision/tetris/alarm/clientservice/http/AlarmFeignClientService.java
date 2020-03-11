package com.sumavision.tetris.alarm.clientservice.http;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;
import com.sumavision.tetris.alarm.bo.http.SubscribeParamBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.JsonBodyWrapper;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class AlarmFeignClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmFeignClientService.class);

	private static AlarmFeignClientService alarmClientService;

	@Value("${spring.application.name}")
	private String serviceName;

	@Value("${spring.cloud.client.ipAddress}")
	private String sourceServiceIP;

	@Autowired
	private AlarmFeign alarmFeign;

	/**
	 * 初始化Service
	 * 
	 * @param serviceName 本微服务名称，标识告警、操作日志的来源
	 * @param localIP     本地业务IP，不用于通信，只是标识告警、操作日志的来源
	 * @return
	 */
	// public static AlarmClientService init(String serviceName, String localIP)
	// throws Exception {

	/*
	 * if (StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(localIP)) { throw
	 * new IllegalArgumentException("serviceName Should not be NULL!"); }
	 * 
	 * if (alarmClientService == null) { synchronized (AlarmClientService.class) {
	 * if (alarmClientService == null) { alarmClientService = new
	 * AlarmClientService(); } } }
	 * 
	 * alarmClientService.setServiceName(serviceName);
	 * alarmClientService.setSourceIP(localIP);
	 * 
	 * return alarmClientService;
	 */
	// }

	/**
	 * 获取唯一操作对象，注意，没有init时返回的就是null
	 * 
	 * @return
	 */
	// public static AlarmFeignClientService getInstance() {
	// return alarmClientService;
	// }

	/**
	 * 触发告警消息
	 * 
	 * @param alarmCode   告警编码
	 * @param alarmDevice 告警设备
	 * @param alarmObj    告警对象
	 * @param params      告警参数 （只用于查询显示，供用户分析问题） 可空
	 * @param isOnce      是否是不需要恢复的告警，可空，默认false
	 * @param createTime  告警创建时间，可空
	 * @throws Exception
	 */
	public void triggerAlarm(String alarmCode, String alarmDevice, String alarmObj, Map<String, String> params,
			Boolean isOnce, Date createTime) throws Exception {

		if (null == alarmCode) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		AlarmParamBO alarmParamBO = new AlarmParamBO();
		alarmParamBO.setAlarmCode(alarmCode);
		alarmParamBO.setSourceService(serviceName);
		alarmParamBO.setAlarmDevice(alarmDevice);
		alarmParamBO.setAlarmObj(alarmObj);
		alarmParamBO.setParams(params);
		alarmParamBO.setSourceServiceIP(sourceServiceIP);

		// createTime 可空
		if (createTime != null) {
			alarmParamBO.setCreateTime(createTime);
		}

		if (isOnce == null || !isOnce) {
			alarmParamBO.setAlarmStatus("UNTREATED");
		} else {
			alarmParamBO.setAlarmStatus("ONCE");
		}

		alarmFeign.triggerAlarm(alarmParamBO);

	}

	/**
	 * 发送告警恢复消息
	 * 
	 * @param alarmCode    告警编码
	 * @param alarmDevice  告警设备
	 * @param alarmObj     告警对象
	 * @param params       恢复参数（只用于查询显示，供用户分析问题） 可空
	 * @param recoveryTime 恢复时间 可空
	 * @throws Exception
	 */
	public void recoverAlarm(String alarmCode, String alarmDevice, String alarmObj, Map<String, String> params,
			Date recoveryTime) throws Exception {

		if (StringUtils.isEmpty(alarmCode) || StringUtils.isEmpty(alarmObj) || StringUtils.isEmpty(alarmDevice)) {
			throw new IllegalArgumentException("Alarm Code or SourceObj Should not be NULL!");
		}

		AlarmParamBO alarmParamBO = new AlarmParamBO();
		alarmParamBO.setAlarmCode(alarmCode);
		alarmParamBO.setSourceService(serviceName);
		alarmParamBO.setAlarmDevice(alarmDevice);
		alarmParamBO.setAlarmObj(alarmObj);
		alarmParamBO.setParams(params);
		alarmParamBO.setSourceServiceIP(sourceServiceIP);

		if (recoveryTime != null) {
			alarmParamBO.setCreateTime(recoveryTime);
		}

		alarmFeign.recoverAlarm(alarmParamBO);

	}

	/**
	 * 订阅告警
	 * 
	 * @param alarmCode
	 * @param callBackUrl
	 * @throws Exception
	 */
	public void subscribeAlarm(String alarmCode, String callBackUrl, boolean notify_everytime) throws Exception {

		if (StringUtils.isEmpty(alarmCode)) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		SubscribeParamBO subscribeRequestParam = new SubscribeParamBO();
		subscribeRequestParam.setSourceService(serviceName);
		subscribeRequestParam.setAlarmCode(alarmCode);
		subscribeRequestParam.setSubscribeIP(sourceServiceIP);
		subscribeRequestParam.setCallbackUrl(callBackUrl);
		subscribeRequestParam.setAlarmNotifyMethod("RIBBON_LOADBALANCED");
		if (notify_everytime) {
			subscribeRequestParam.setAlarmNotifyPattern("NOTIFY_EVERYTIME");
		} else {
			subscribeRequestParam.setAlarmNotifyPattern("NOTIFY_NORMAL");
		}

		alarmFeign.subscribeAlarm(subscribeRequestParam);

	}

	/**
	 * 
	 * 发送 取消告警订阅 消息
	 * 
	 * @param alarmCode 告警编码
	 * @throws Exception
	 */
	public void unSubscribeAlarm(String alarmCode) throws Exception {

		if (StringUtils.isEmpty(alarmCode)) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		SubscribeParamBO subscribeRequestParam = new SubscribeParamBO();
		subscribeRequestParam.setSourceService(serviceName);
		subscribeRequestParam.setAlarmCode(alarmCode);
		subscribeRequestParam.setSourceService(sourceServiceIP);

		alarmFeign.unSubscribeAlarm(subscribeRequestParam);

		// TODO
	}

	/**
	 * 
	 * 
	 * @param userName 用户名称
	 * @param oprName  操作名称
	 * @param detail   操作详情 可空
	 * @param oprTime  操作时间 可空
	 * 
	 */
	public void sendOprLog(String userName, String oprName, String detail, Date oprTime) throws Exception {

		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(oprName)) {
			throw new IllegalArgumentException("UserName or OprName Should not be Empty!");
		}

		OprlogParamBO oprlogParamBO = new OprlogParamBO();
		oprlogParamBO.setUserName(userName);
		oprlogParamBO.setSourceService(serviceName);
		oprlogParamBO.setOprName(oprName);
		oprlogParamBO.setOprDetail(detail);
		oprlogParamBO.setSourceServiceIP(sourceServiceIP);

		if (oprTime != null) {
			oprlogParamBO.setOprTime(oprTime);
		}

		alarmFeign.sendOprlog(oprlogParamBO);

	}

	public List<AlarmNotifyBO> querySubUntreatedAlarm() throws Exception {
		if (StringUtils.isEmpty(serviceName)) {
			throw new IllegalArgumentException("service name Should not be NULL!");
		}

		return JsonBodyResponseParser.parseArray(alarmFeign.querySubUntreatedAlarms(serviceName), AlarmNotifyBO.class);

	}

	public static AlarmFeignClientService getAlarmClientService() {
		return alarmClientService;
	}

	public static void setAlarmClientService(AlarmFeignClientService alarmClientService) {
		AlarmFeignClientService.alarmClientService = alarmClientService;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSourceServiceIP() {
		return sourceServiceIP;
	}

	public void setSourceServiceIP(String sourceServiceIP) {
		this.sourceServiceIP = sourceServiceIP;
	}

}
