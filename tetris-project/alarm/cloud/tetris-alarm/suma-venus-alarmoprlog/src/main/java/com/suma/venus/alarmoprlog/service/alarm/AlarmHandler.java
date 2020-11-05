package com.suma.venus.alarmoprlog.service.alarm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.IRawAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.service.alarm.notify.AlarmNotifyThreadPool;
import com.suma.venus.alarmoprlog.service.alarm.notify.HttpAlarmNotifyThread;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;

@Service
public class AlarmHandler {

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private IAlarmDAO alarmDAO;

	@Autowired
	private IAlarmInfoDAO alarmInfoDAO;

	@Autowired
	private IRawAlarmDAO rawAlarmDAO;

	@Autowired
	private ISubscribeAlarmDAO subscribeAlarmDAO;

	@Autowired
	@LoadBalanced
	private RestTemplate loadBalanced;

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmHandler.class);

	public void handleAlarm(AlarmParamBO alarmParamBO) {

		if (alarmParamBO == null || alarmParamBO.getType() == null || alarmParamBO.getAlarmCode() == null) {
			return;
		}

		if (alarmParamBO.getType().equals("alarm")) {

			// 获得alarmInfo
			String alarmCode = alarmParamBO.getAlarmCode();
			AlarmInfoPO alarmInfoPO = alarmInfoDAO.findByAlarmCode(alarmCode);

			if (alarmInfoPO == null) {
				LOGGER.warn("Cannot query any alarmInfo by alarmCode[{}]", alarmCode);
				return;
			}

			List<AlarmPO> alarmPOs = alarmService.queryAlarmPOforNewMsg(alarmParamBO, EAlarmStatus.UNTREATED);

			// 组装rawAlarm
			RawAlarmPO rawAlarmPO = new RawAlarmPO();
			rawAlarmPO.setAlarmInfo(alarmInfoPO);
			rawAlarmPO.setSourceServiceIP(alarmParamBO.getSourceServiceIP());
			rawAlarmPO.setSourceService(alarmParamBO.getSourceService());
			rawAlarmPO.setAlarmDevice(alarmParamBO.getAlarmDevice());
			rawAlarmPO.setAlarmObj(alarmParamBO.getAlarmObj());
			rawAlarmPO.setAlarmParams(formateParams(alarmParamBO.getParams()));
			rawAlarmPO.setCreateTime(alarmParamBO.getCreateTime());

			rawAlarmDAO.save(rawAlarmPO);

			LOGGER.info("----------rawAlarmPO saved");

			Long rawAlarmId = rawAlarmPO.getId();

			if (rawAlarmId == null || rawAlarmId == -1L) {
				LOGGER.error("fail to create rawAlarm");
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
					LOGGER.error("fail to create alarm");
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

			HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(alarmPO,
					null, subscribeAlarmDAO,
					alarmDAO, loadBalanced, restTemplate, false);
			// 放入线程池待执行
			AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

			LOGGER.info("----------handleTrigger finish");
			return;

		} else if (alarmParamBO.getType().equals("recover")) {

			List<AlarmPO> alarmPOs = alarmService.queryAlarmPOforNewMsg(alarmParamBO, EAlarmStatus.UNTREATED);

			if (CollectionUtils.isEmpty(alarmPOs)) {
				// TODO 要不要考虑异步的情况
				return;
			}

			AlarmPO alarmPO = alarmPOs.get(0);
			alarmPO.setRecoverTime(alarmParamBO.getCreateTime());
			alarmPO.setAlarmStatus(EAlarmStatus.AUTO_RECOVER);

			alarmDAO.save(alarmPO);

			// AlarmNotifyHandleThread alarmNotifyHandleThread = new
			// AlarmNotifyHandleThread(alarmPO, subscribeAlarmDAO,
			// alarmDAO, messageService);

			// 放入线程池待执行
			// AlarmNotifyThreadPool.getThreadPool().execute(alarmNotifyHandleThread);

			HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(alarmPO, null, subscribeAlarmDAO,
					alarmDAO, loadBalanced, restTemplate, false);
			// 放入线程池待执行
			AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

			LOGGER.info("----------handleRecovery finish");
			return;
		}

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

}
