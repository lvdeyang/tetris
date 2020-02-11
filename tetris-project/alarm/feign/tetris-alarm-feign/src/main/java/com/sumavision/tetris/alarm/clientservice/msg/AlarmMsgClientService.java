package com.sumavision.tetris.alarm.clientservice.msg;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.service.MessageService;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;
import com.sumavision.tetris.alarm.bo.msg.AlarmMsgBO;
import com.sumavision.tetris.alarm.bo.msg.SubscribeRequestParamBO;

/**
 * 告警、操作日志客户端主操作类 包括初始化，获取实例和告警、操作日志客户端所有操作方法 单例
 * 
 * @author chenmo
 *
 */
public class AlarmMsgClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmMsgClientService.class);

	private String serviceName;

	private String sourceID;

	private String sourceIP;

	private static AlarmMsgClientService alarmClientService;

	private MessageService messageService;

	private String alarmoprlogDstID = "suma-venus-alarmoprlog";

	private long ALARM_MSG_TIMEOUT = 1 * 24 * 3600 * 1000l;

	private long OPRLOG_MSG_TIMEOUT = 1 * 24 * 3600 * 1000l;

	private AlarmMsgClientService() {

	}

	/**
	 * 初始化Service
	 * 
	 * @param serviceName    本微服务名称，标识告警、操作日志的来源
	 * @param sourceID       本地业务在消息服务的ID，用于告警等的通知
	 * @param localIP        本地业务IP，不用于通信，只是标识告警、操作日志的来源
	 * @param messageService 消息服务的Service
	 * @return
	 */
	public static AlarmMsgClientService initClient(String serviceName, String sourceID, String localIP,
			MessageService messageService) throws Exception {

		if (Strings.isNullOrEmpty(serviceName) || Strings.isNullOrEmpty(sourceID) || Strings.isNullOrEmpty(localIP)) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		if (alarmClientService == null) {
			synchronized (AlarmMsgClientService.class) {
				if (alarmClientService == null) {
					alarmClientService = new AlarmMsgClientService();
				}
			}
		}

		alarmClientService.setServiceName(serviceName);
		alarmClientService.setSourceIP(localIP);
		alarmClientService.setSourceID(sourceID);
		alarmClientService.setMessageService(messageService);
		return alarmClientService;
	}

	/**
	 * 获取唯一操作对象，注意，没有init时返回的就是null
	 * 
	 * @return
	 */
	public static AlarmMsgClientService getInstance() {

		return alarmClientService;

	}

	/**
	 * 触发告警消息
	 * 
	 * @param alarmCode  告警编码
	 * @param sourceObj  告警来源
	 * @param params     告警详细参数 可空
	 * @param createTime 告警时间 可空
	 * 
	 */
	public void triggerAlarm(String alarmCode, String alarmDevice, String alarmObj, Map<String, String> params,
			Date createTime) {

		if (null == alarmCode) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		try {
			AlarmParamBO alarmParamBO = new AlarmParamBO();
			alarmParamBO.setAlarmCode(alarmCode);
			alarmParamBO.setSourceService(serviceName);
			alarmParamBO.setAlarmDevice(alarmDevice);
			alarmParamBO.setAlarmObj(alarmObj);
			alarmParamBO.setParams(params);
			alarmParamBO.setSourceServiceIP(sourceIP);

			// createTime 可空
			if (createTime != null) {
				alarmParamBO.setCreateTime(createTime);
			}

			AlarmMsgBO alarmMsg = new AlarmMsgBO();
			alarmMsg.getMessage().getMessage_header().setSource_id(sourceID);
			alarmMsg.getMessage().getMessage_header().setDestination_id(alarmoprlogDstID);
			alarmMsg.getMessage().getMessage_header().setMessage_name("triggerAlarm");

			// TODO
			// 组装方式别扭。。。
			alarmMsg.getMessage().getMessage_body().put("trigger_alarm_param", alarmParamBO);

			String msg = JSONObject.toJSONString(alarmMsg);

			messageService.msgSend2SingleNode(alarmoprlogDstID, msg, ALARM_MSG_TIMEOUT, null);
		} catch (Exception e) {

			LOGGER.error("triggerAlarm error: " + e);
		}
	}

	/**
	 * 发送告警恢复消息
	 * 
	 * @param alarmCode    告警编码
	 * @param sourceObj    告警来源
	 * @param params       恢复参数（只用于查询显示，供用户分析问题） 可空
	 * @param recoveryTime 恢复时间 可空
	 * 
	 */
	public void recoverAlarm(String alarmCode, String alarmDevice,String alarmObj, Map<String, String> params, Date recoveryTime) {

		if (Strings.isNullOrEmpty(alarmCode) || Strings.isNullOrEmpty(alarmObj)) {
			throw new IllegalArgumentException("Alarm Code or SourceObj Should not be NULL!");
		}
		try {
			AlarmParamBO alarmParamBO = new AlarmParamBO();
			alarmParamBO.setAlarmCode(alarmCode);
			alarmParamBO.setSourceService(serviceName);
			alarmParamBO.setAlarmDevice(alarmDevice);
			alarmParamBO.setAlarmObj(alarmObj);
			alarmParamBO.setParams(params);
			alarmParamBO.setSourceServiceIP(sourceIP);

			if (recoveryTime != null) {
				alarmParamBO.setCreateTime(recoveryTime);
			}

			AlarmMsgBO alarmMsg = new AlarmMsgBO();
			alarmMsg.getMessage().getMessage_header().setSource_id(sourceID);
			alarmMsg.getMessage().getMessage_header().setDestination_id(alarmoprlogDstID);
			alarmMsg.getMessage().getMessage_header().setMessage_name("recoverAlarm");

			alarmMsg.getMessage().getMessage_body().put("recover_alarm_param", alarmParamBO);

			String msg = JSONObject.toJSONString(alarmMsg);

			System.out.println(Calendar.getInstance().getTime().toString() + "**************start");

			messageService.msgSend2SingleNode(alarmoprlogDstID, msg, ALARM_MSG_TIMEOUT, null);

			System.out.println(Calendar.getInstance().getTime().toString() + "**************end");

		} catch (Exception e) {
			LOGGER.error("recoverAlarm error: " + e);

		}

	}

	/**
	 * 发送告警订阅消息
	 *
	 * @param alarmCode 告警编码
	 * @return ResponseBO对象
	 * @throws Exception
	 */
	public ResponseBO subscribeAlarm(String alarmCode) throws Exception {

		if (Strings.isNullOrEmpty(alarmCode)) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		SubscribeRequestParamBO subscribeRequestParam = new SubscribeRequestParamBO();
		subscribeRequestParam.setSourceService(serviceName);
		subscribeRequestParam.setAlarmCode(alarmCode);
		subscribeRequestParam.setIp(sourceIP);

		RequestBaseBO request = new RequestBaseBO();
		request.getMessage().getMessage_header().setSource_id(sourceID);
		request.getMessage().getMessage_header().setDestination_id(alarmoprlogDstID);
		request.getMessage().getMessage_header().setMessage_name("subscribeAlarm");

		request.getMessage().getMessage_body().put("subscibe_alarm_request", subscribeRequestParam);

		ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(alarmoprlogDstID, request, 1000 * 30);

		// System.out.println(responseBO.toString());

		return responseBO;
	}

	/**
	 * 
	 * 发送 取消告警订阅 消息
	 * 
	 * @param alarmCode 告警编码
	 * @return ResponseBO对象
	 * @throws Exception
	 */
	public ResponseBO unSubscribeAlarm(String alarmCode) throws Exception {

		if (Strings.isNullOrEmpty(alarmCode)) {
			throw new IllegalArgumentException("Alarm Code Should not be NULL!");
		}

		SubscribeRequestParamBO subscribeRequestParam = new SubscribeRequestParamBO();
		subscribeRequestParam.setSourceService(serviceName);
		subscribeRequestParam.setAlarmCode(alarmCode);
		subscribeRequestParam.setIp(sourceIP);

		RequestBaseBO request = new RequestBaseBO();
		request.getMessage().getMessage_header().setSource_id(sourceID);
		request.getMessage().getMessage_header().setDestination_id(alarmoprlogDstID);
		request.getMessage().getMessage_header().setMessage_name("unSubscribeAlarm");

		request.getMessage().getMessage_body().put("unsubscibe_alarm_request", subscribeRequestParam);

		ResponseBO responseBO = messageService.msgSend2SingleNodeAndWaitResp(alarmoprlogDstID, request, 1000 * 30);

		return responseBO;

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
	public void sendOprLog(String userName, String oprName, String detail, Date oprTime) {

		if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(oprName)) {
			throw new IllegalArgumentException("UserName or OprName Should not be Empty!");
		}

		try {
			OprlogParamBO oprlogParamBO = new OprlogParamBO();
			oprlogParamBO.setUserName(userName);
			oprlogParamBO.setSourceService(serviceName);
			oprlogParamBO.setOprName(oprName);
			oprlogParamBO.setOprDetail(detail);
			oprlogParamBO.setIp(sourceIP);

			if (oprTime != null) {
				oprlogParamBO.setOprTime(oprTime);
			}

			AlarmMsgBO alarmMsg = new AlarmMsgBO();
			alarmMsg.getMessage().getMessage_header().setSource_id(sourceID);
			alarmMsg.getMessage().getMessage_header().setDestination_id(alarmoprlogDstID);
			alarmMsg.getMessage().getMessage_header().setMessage_name("oprlog");

			alarmMsg.getMessage().getMessage_body().put("oprlog_param", oprlogParamBO);

			String msg = JSONObject.toJSONString(alarmMsg);

			messageService.msgSend2SingleNode(alarmoprlogDstID, msg, OPRLOG_MSG_TIMEOUT, null);
		} catch (Exception e) {

			LOGGER.error("sendOprLog error: " + e);
		}

	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
