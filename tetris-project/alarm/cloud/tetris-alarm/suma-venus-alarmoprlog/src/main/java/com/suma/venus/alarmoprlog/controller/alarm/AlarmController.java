package com.suma.venus.alarmoprlog.controller.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.IRawAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EBlockStatus;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.service.alarm.AlarmService;
import com.suma.venus.alarmoprlog.service.alarm.notify.AlarmNotifyThreadPool;
import com.suma.venus.alarmoprlog.service.alarm.notify.HttpAlarmNotifyThread;
import com.suma.venus.alarmoprlog.service.alarm.vo.AlarmVO;
import com.suma.venus.alarmoprlog.service.alarm.vo.QueryAlarmVO;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;

@Controller
@RequestMapping("/alarm")
public class AlarmController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmController.class);

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

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tiggerAlarm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> httpTriggerAlarm(@RequestBody AlarmParamBO alarmParamBO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();
		
		LOGGER.warn("alarmParamBO="+ JSONObject.toJSONString(alarmParamBO));
		
		// 获得alarmInfo
		String alarmCode = alarmParamBO.getAlarmCode();
		AlarmInfoPO alarmInfoPO = alarmInfoDAO.findByAlarmCode(alarmCode);

		if (alarmInfoPO == null) {
			LOGGER.warn("Cannot query any alarmInfo by alarmCode[{}]", alarmCode);
			data.put("errMsg", "do not support this alarm type");
			return data;
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
			data.put("errMsg", "");
			return data;
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
				data.put("errMsg", "");
				return data;
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

		HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(alarmPO, subscribeAlarmDAO, alarmDAO,
				loadBalanced, restTemplate);
		// 放入线程池待执行
		AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

		LOGGER.info("----------handleTrigger finish");
		data.put("errMsg", "");
		return data;
	}

	@RequestMapping(value = "/recoverAlarm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> httpRecoverAlarm(@RequestBody AlarmParamBO alarmParamBO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		LOGGER.info("----------handleRecovery start, alarmCode==" + alarmParamBO.getAlarmCode() + ", service=="
				+ alarmParamBO.getSourceService() + ", sourceServiceIP==" + alarmParamBO.getSourceServiceIP()
				+ ", alarmObj==" + alarmParamBO.getAlarmObj() + ", creatTime==" + alarmParamBO.getCreateTime());

		List<AlarmPO> alarmPOs = alarmService.queryAlarmPOforNewMsg(alarmParamBO, EAlarmStatus.UNTREATED);

		if (CollectionUtils.isEmpty(alarmPOs)) {
			// TODO 要不要考虑异步的情况
			data.put("errMsg", "");
			return data;
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

		HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(alarmPO, subscribeAlarmDAO, alarmDAO,
				loadBalanced, restTemplate);
		// 放入线程池待执行
		AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

		LOGGER.info("----------handleRecovery finish");
		return data;
	}

	/**
	 * 按条件查询告警
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryPage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAlarm(@ModelAttribute QueryAlarmVO queryAlarmVO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		// TODO
		// Pageable pageable = PageRequest.of(queryAlarmVO.getPageIndex(),
		// queryAlarmVO.getPageSize(), Sort.Direction.DESC,
		// "id");

		Pageable pageable = new PageRequest(queryAlarmVO.getPageIndex(), queryAlarmVO.getPageSize(),
				Sort.Direction.DESC, "id");

		try {

			Page<AlarmPO> alarmPOPage = alarmService.queryAlarmByQueryVOPage(queryAlarmVO, pageable);

			List<AlarmVO> alarmVOs = new ArrayList<>();

			if (!alarmPOPage.getContent().isEmpty()) {
				alarmVOs = AlarmVO.transFromPOs(alarmPOPage.getContent(), "en-EN");
			}

			data.put("errMsg", "");
			data.put("total", alarmPOPage.getTotalElements());
			data.put("alarmVOs", JSONObject.toJSON(alarmVOs));

		} catch (Exception e) {
			LOGGER.error("AlarmController queryAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ignore", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ignoreAlarm(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Optional<AlarmPO> alarmOptional = alarmDAO.findById(id);
			if (!alarmOptional.isPresent()) {
				data.put("errMsg", "内部错误");
			} else {
				AlarmPO alarmPO = alarmOptional.get();
				alarmPO.setAlarmStatus(EAlarmStatus.IGNORE);
				alarmPO.setRecoverTime(Calendar.getInstance().getTime());
				alarmDAO.save(alarmPO);
				data.put("errMsg", "");
			}

		} catch (Exception e) {
			LOGGER.error("AlarmController ignoreAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 页面操作恢复告警
	 * 
	 * @return
	 */
	@RequestMapping(value = "/manualRecover", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> manualRecoverAlarm(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Optional<AlarmPO> alarmOptional = alarmDAO.findById(id);

			if (!alarmOptional.isPresent()) {
				data.put("errMsg", "内部错误");
				return data;
			}

			AlarmPO alarmPO = alarmOptional.get();
			alarmPO.setAlarmStatus(EAlarmStatus.MANUAL_RECOVER);
			alarmPO.setRecoverTime(Calendar.getInstance().getTime());
			alarmDAO.save(alarmPO);

			HttpAlarmNotifyThread httpAlarmNotifyThread = new HttpAlarmNotifyThread(alarmPO, subscribeAlarmDAO,
					alarmDAO, loadBalanced, restTemplate);
			// 放入线程池待执行
			AlarmNotifyThreadPool.getThreadPool().execute(httpAlarmNotifyThread);

			data.put("errMsg", "");

		} catch (Exception e) {
			LOGGER.error("AlarmController manualRecoverAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 页面操作屏蔽告警
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shield", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> shieldAlarm(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			Optional<AlarmPO> alarmOptional = alarmDAO.findById(id);

			if (!alarmOptional.isPresent()) {
				data.put("errMsg", "内部错误");
				return data;
			}

			AlarmInfoPO alarmInfoPO = alarmOptional.get().getLastAlarm().getAlarmInfo();
			alarmInfoPO.setBlockStatus(EBlockStatus.BLOCKED);
			alarmInfoDAO.save(alarmInfoPO);

			data.put("errMsg", "");

		} catch (Exception e) {
			LOGGER.error("AlarmController shieldAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	/**
	 * 页面操作屏蔽告警
	 * 
	 * @return
	 */
	@RequestMapping(value = "/unShield", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unShieldAlarm(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			Optional<AlarmPO> alarmOptional = alarmDAO.findById(id);

			if (!alarmOptional.isPresent()) {
				data.put("errMsg", "内部错误");
				return data;
			}

			AlarmInfoPO alarmInfoPO = alarmOptional.get().getLastAlarm().getAlarmInfo();
			alarmInfoPO.setBlockStatus(EBlockStatus.NORMAL);
			alarmInfoDAO.save(alarmInfoPO);

			data.put("errMsg", "");

		} catch (Exception e) {
			LOGGER.error("AlarmController unShieldAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	/**
	 * 页面操作删除告警
	 * 
	 * @return
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delAlarm(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			Optional<AlarmPO> alarmOptional = alarmDAO.findById(id);

			if (!alarmOptional.isPresent()) {
				data.put("errMsg", "内部错误");
				return data;
			}

			// rawAlarmPO一同删除
			Long alarmPOId = alarmOptional.get().getId();

			alarmDAO.deleteById(alarmPOId);

			List<RawAlarmPO> rawAlarmPOs = rawAlarmDAO.findByAlarmPOId(alarmPOId);

			// TODO
			// rawAlarmDAO.deleteInBatch(rawAlarmPOs);

			data.put("errMsg", "");

		} catch (Exception e) {
			LOGGER.error("AlarmController delAlarm() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
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
