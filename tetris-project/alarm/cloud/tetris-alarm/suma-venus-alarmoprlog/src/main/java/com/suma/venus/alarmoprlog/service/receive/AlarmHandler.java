package com.suma.venus.alarmoprlog.service.receive;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.IRawAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.service.alarm.AlarmService;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.alarm.bo.http.SubscribeParamBO;

/**
 * 告警处理类
 * 
 * @author chenmo
 *
 */

// @Service
/*
public class AlarmHandler {

	@Autowired
	private IAlarmInfoDAO alarminfoDAO;

	@Autowired
	private IAlarmDAO alarmDAO;

	@Autowired
	private IRawAlarmDAO rawAlarmDAO;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private ISubscribeAlarmDAO subscribeAlarmDAO;

	// @Autowired
	// private MessageService messageService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmHandler.class);

	public void handleTrigger(VenusMessage triggerAlarmMsg) throws InterruptedException {

		AlarmParamBO alarmParamBO = triggerAlarmMsg.getMessage_body().getObject("trigger_alarm_param",
				AlarmParamBO.class);

		LOGGER.info("----------handleTrigger start, alarmCode==" + alarmParamBO.getAlarmCode() + ", service=="
				+ alarmParamBO.getSourceService() + ", ip==" + alarmParamBO.getIp() + ", sourceObj=="
				+ alarmParamBO.getSourceObj() + ", creatTime==" + alarmParamBO.getCreateTime());

		// 获得alarmInfo
		String alarmCode = alarmParamBO.getAlarmCode();
		AlarmInfoPO alarmInfoPO = alarminfoDAO.findByAlarmCode(alarmCode);

		if (alarmInfoPO == null) {
			LOGGER.warn("Cannot query any alarmInfo by alarmCode[{}]", alarmCode);
			return;
		}

		// 查询alarmPO 告警类
		List<AlarmPO> alarmPOs = alarmService.queryAlarmPOforNewMsg(alarmParamBO, EAlarmStatus.UNTREATED);

		// 组装rawAlarm
		RawAlarmPO rawAlarmPO = new RawAlarmPO();
		rawAlarmPO.setAlarmInfo(alarmInfoPO);
		rawAlarmPO.setSourceIP(alarmParamBO.getIp());
		rawAlarmPO.setSourceService(alarmParamBO.getSourceService());
		rawAlarmPO.setSourceObj(alarmParamBO.getSourceObj());
		rawAlarmPO.setAlarmParams(formateParams(alarmParamBO.getParams()));
		rawAlarmPO.setCreateTime(alarmParamBO.getCreateTime());

		rawAlarmDAO.save(rawAlarmPO);

		LOGGER.info("----------rawAlarmPO saved");

		Long rawAlarmId = rawAlarmPO.getId();

		if (rawAlarmId == null || rawAlarmId == -1L) {
			// LOGGER.error("fail to create rawAlarm");
			return;
		}

		RawAlarmPO rawAlarmPO2 = rawAlarmPO;

		AlarmPO alarmPO = null;

		if (alarmPOs == null || alarmPOs.isEmpty()) {

			alarmPO = new AlarmPO();
			alarmPO.setLastAlarm(rawAlarmPO2);
			alarmPO.setFirstCreateTime(rawAlarmPO2.getCreateTime());
			alarmPO.setAlarmStatus(EAlarmStatus.UNTREATED);
			alarmPO.setAlarmCount(1);

			alarmDAO.save(alarmPO);

			Long alarmId = alarmPO.getId();
			// 过滤，创建或者修改实时告警Alarm
			if (alarmId == null || alarmId == -1L) {
				// LOGGER.error("fail to create alarm");
				return;
			}

		} else {

			alarmPO = alarmPOs.get(0);

			alarmPO.setAlarmCount(alarmPO.getAlarmCount() + 1);
			alarmPO.setLastAlarm(rawAlarmPO2);

			alarmDAO.save(alarmPO);

		}

		Long alarmPOId = alarmPO.getId();
		rawAlarmPO2.setAlarmPOId(alarmPOId);
		rawAlarmDAO.save(rawAlarmPO2);

		

		
		// AlarmNotifyHandleThread alarmNotifyHandleThread = new AlarmNotifyHandleThread(alarmPO, subscribeAlarmDAO,
		// 		alarmDAO, messageService);

		// 放入线程池待执行
		// AlarmNotifyThreadPool.getThreadPool().execute(alarmNotifyHandleThread);
		
		LOGGER.info("----------handleTrigger finish");

	}

	public void handleRecovery(VenusMessage recoverAlarmMsg) {

		AlarmParamBO alarmParamBO = recoverAlarmMsg.getMessage_body().getObject("recover_alarm_param",
				AlarmParamBO.class);

		LOGGER.info("----------handleRecovery start, alarmCode==" + alarmParamBO.getAlarmCode() + ", service=="
				+ alarmParamBO.getSourceService() + ", ip==" + alarmParamBO.getIp() + ", sourceObj=="
				+ alarmParamBO.getSourceObj() + ", creatTime==" + alarmParamBO.getCreateTime());

		List<AlarmPO> alarmPOs = alarmService.queryAlarmPOforNewMsg(alarmParamBO, EAlarmStatus.UNTREATED);

		if (CollectionUtils.isEmpty(alarmPOs)) {
			// TODO 要不要考虑异步的情况
			return;
		}

		AlarmPO alarmPO = alarmPOs.get(0);
		alarmPO.setRecoverTime(alarmParamBO.getCreateTime());
		alarmPO.setAlarmStatus(EAlarmStatus.AUTO_RECOVER);

		alarmDAO.save(alarmPO);

		// AlarmNotifyHandleThread alarmNotifyHandleThread = new AlarmNotifyHandleThread(alarmPO, subscribeAlarmDAO,
		//		alarmDAO, messageService);

		// 放入线程池待执行
		// AlarmNotifyThreadPool.getThreadPool().execute(alarmNotifyHandleThread);
		
		LOGGER.info("----------handleRecovery finish");


	}

	public void handleSubscribe(VenusMessage requestMsg) throws Exception {

		// messageHead 已验证
		VenusMessageHead messageHead = requestMsg.getMessage_header();

		SubscribeParamBO subscribeMsgParam = JSONObject.parseObject(
				requestMsg.getMessage_body().get("subscibe_alarm_request").toString(), SubscribeParamBO.class);

		LOGGER.info("----------handleSubscribe start, alarmCode==" + subscribeMsgParam.getAlarmCode() + ", service=="
				+ subscribeMsgParam.getSourceService() + ", ip==" + subscribeMsgParam.getIp() + ", subTime=="
				+ subscribeMsgParam.getSubscribeTime());

		if (subscribeMsgParam == null || StringUtils.isEmpty(subscribeMsgParam.getAlarmCode())) {
			ResponseBO responseBO = new ResponseBO(-1, messageHead.getSource_id(), messageHead.getSequence_id());
			responseBO.setResp("alarmCode illeage");

			// messageService.writeResp(messageHead.getSource_id(), responseBO, messageHead.getSource_selector_id());
			return;

		}

		AlarmInfoPO alarmInfoPO = alarminfoDAO.findByAlarmCode(subscribeMsgParam.getAlarmCode());

		if (alarmInfoPO == null) {
			ResponseBO responseBO = new ResponseBO(-1, messageHead.getSource_id(), messageHead.getSequence_id());
			responseBO.setResp("alarmCode illeage");

			// messageService.writeResp(messageHead.getSource_id(), responseBO, messageHead.getSource_selector_id());
			return;
		}

		List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO.findByAlarmInfo_AlarmCodeAndSubServiceNameAndDstId(
				subscribeMsgParam.getAlarmCode(), subscribeMsgParam.getSourceService(), messageHead.getSource_id());

		if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {

			SubscribeAlarmPO subscribeAlarmPO = new SubscribeAlarmPO();
			subscribeAlarmPO.setAlarmInfo(alarmInfoPO);
			subscribeAlarmPO.setDstId(messageHead.getSource_id());
			subscribeAlarmPO.setSubServiceName(subscribeMsgParam.getSourceService());
			subscribeAlarmPO.setSubsIP(subscribeMsgParam.getIp());
			subscribeAlarmPO.setSubsTime(subscribeMsgParam.getSubscribeTime());
			subscribeAlarmDAO.save(subscribeAlarmPO);
		} else {

			SubscribeAlarmPO subscribeAlarmPO2 = subscribeAlarmPOs.get(0);
			subscribeAlarmPO2.setSubsTime(subscribeMsgParam.getSubscribeTime());
			subscribeAlarmDAO.save(subscribeAlarmPO2);
		}

		ResponseBO responseBO = new ResponseBO(0, messageHead.getSource_id(), messageHead.getSequence_id());

		// messageService.writeResp(messageHead.getSource_id(), responseBO, messageHead.getSource_selector_id());
		
		LOGGER.info("----------handleSubscribe finish");


	}

	public void handleUnSubscribe(VenusMessage requestMsg) throws Exception {

		// messageHead 已验证
		VenusMessageHead messageHead = requestMsg.getMessage_header();

		SubscribeParamBO subscribeMsgParam = (SubscribeParamBO) requestMsg.getMessage_body()
				.get("unsubscibe_alarm_request");

		LOGGER.info("----------handleUnSubscribe start, alarmCode==" + subscribeMsgParam.getAlarmCode() + ", service=="
				+ subscribeMsgParam.getSourceService() + ", ip==" + subscribeMsgParam.getIp() + ", subTime=="
				+ subscribeMsgParam.getSubscribeTime());

		List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO.findByAlarmInfo_AlarmCodeAndSubServiceNameAndDstId(
				subscribeMsgParam.getAlarmCode(), subscribeMsgParam.getSourceService(), messageHead.getSource_id());

		if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {

			ResponseBO responseBO = new ResponseBO(-1, messageHead.getSource_id(), messageHead.getSequence_id());
			responseBO.setResp("subscribe alarm not found");
			// messageService.writeResp(messageHead.getSource_id(), responseBO, messageHead.getSource_selector_id());

			return;
		}

		subscribeAlarmDAO.delete(subscribeAlarmPOs.get(0));
		ResponseBO responseBO = new ResponseBO(0, messageHead.getSource_id(), messageHead.getSequence_id());
		// messageService.writeResp(messageHead.getSource_id(), responseBO, messageHead.getSource_selector_id());
		
		LOGGER.info("----------handleSubscribe finish");

	}

	public String formateParams(Map<String, String> params) {
		if (ObjectUtils.isEmpty(params)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
		}

		return sb.toString();
	}

};
*/