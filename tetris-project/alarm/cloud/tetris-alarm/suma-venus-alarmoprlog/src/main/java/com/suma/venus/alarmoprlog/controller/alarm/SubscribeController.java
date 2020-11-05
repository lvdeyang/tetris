package com.suma.venus.alarmoprlog.controller.alarm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.ISubscribeAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyMethod;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyPattern;
import com.suma.venus.alarmoprlog.service.alarm.vo.SubscribeAlarmVO;
import com.sumavision.tetris.alarm.bo.http.SubscribeParamBO;

@Controller
@RequestMapping("/subscribe")
public class SubscribeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeController.class);

	@Autowired
	private ISubscribeAlarmDAO subscribeAlarmDAO;

	@Autowired
	private IAlarmInfoDAO alarmInfoDAO;

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/subscribeAlarm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> subscribeAlarm(@RequestBody SubscribeParamBO subscribeParamBO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		AlarmInfoPO alarmInfoPO = alarmInfoDAO.findByAlarmCode(subscribeParamBO.getAlarmCode());

		if (alarmInfoPO == null) {
			data.put("errMsg", "alarmCode illeage");
			return data;
		}

		// TODO
		List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO
				.findByAlarmInfo_AlarmCodeAndSubServiceNameAndMsgCallbackId(subscribeParamBO.getAlarmCode(),
						subscribeParamBO.getSourceService(), null);

		SubscribeAlarmPO subscribeAlarmPO;

		if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {

			subscribeAlarmPO = new SubscribeAlarmPO();
			subscribeAlarmPO.setAlarmInfo(alarmInfoPO);
			subscribeAlarmPO.setSubServiceName(subscribeParamBO.getSourceService());
		} else {
			subscribeAlarmPO = subscribeAlarmPOs.get(0);
		}

		subscribeAlarmPO.setMsgCallbackId(null);
		subscribeAlarmPO.setSubsTime(subscribeParamBO.getSubscribeTime());
		subscribeAlarmPO.setCallbackUrl(subscribeParamBO.getCallbackUrl());

		if (StringUtils.isEmpty(subscribeParamBO.getAlarmNotifyMethod())) {
			// TODO 默认为普通HTTP方式发送
			subscribeAlarmPO.setAlarmNotifyMethod(EAlarmNotifyMethod.HTTP);
		} else {
			subscribeAlarmPO.setAlarmNotifyMethod(EAlarmNotifyMethod.valueOf(subscribeParamBO.getAlarmNotifyMethod()));
		}

		if (StringUtils.isEmpty(subscribeParamBO.getAlarmNotifyPattern())) {
			subscribeAlarmPO.setAlarmNotifyPattern(EAlarmNotifyPattern.NOTIFY_NORMAL);
		} else {
			subscribeAlarmPO
					.setAlarmNotifyPattern(EAlarmNotifyPattern.valueOf(subscribeParamBO.getAlarmNotifyPattern()));
		}

		subscribeAlarmDAO.save(subscribeAlarmPO);

		data.put("errMsg", "");
		return data;

	}

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/unSubscribeAlarm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unSubscribeAlarm(@RequestBody SubscribeParamBO subscribeParamBO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		List<SubscribeAlarmPO> subscribeAlarmPOs = subscribeAlarmDAO
				.findByAlarmInfo_AlarmCodeAndSubServiceNameAndMsgCallbackId(subscribeParamBO.getAlarmCode(),
						subscribeParamBO.getSourceService(), null);

		if (subscribeAlarmPOs == null || subscribeAlarmPOs.isEmpty()) {
			LOGGER.error("subscribe alarm not found");
			data.put("errMsg", "");
			return data;
		}

		subscribeAlarmDAO.delete(subscribeAlarmPOs.get(0));

		LOGGER.info("----------unSubscribe finish");
		data.put("errMsg", "");
		return data;

	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> del(@RequestParam(value = "id", required = true) Long id) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		try {
			SubscribeAlarmPO subscribeAlarmPO = subscribeAlarmDAO.findOne(id);
			if (subscribeAlarmPO == null) {
				data.put("errMsg", "参数错误");
				return data;
			}

			subscribeAlarmDAO.delete(subscribeAlarmPO);

		} catch (Exception e) {
			data.put("errMsg", "内部错误");
			return data;
		}

		LOGGER.info("----------delsubscribe finish");
		data.put("errMsg", "");
		return data;

	}

	/**
	 * 按告警编码查询订阅信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryByAlarmCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySubscribeByAlarmCode(
			@RequestParam(value = "alarmCode", required = false) String alarmCode) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		try {
			List<SubscribeAlarmPO> subscribeAlarmPOs = null;

			if (StringUtils.isEmpty(alarmCode)) {
				subscribeAlarmPOs = subscribeAlarmDAO.findAll();
			} else {
				subscribeAlarmPOs = subscribeAlarmDAO.findByAlarmInfo_AlarmCode(alarmCode);
			}

			List<SubscribeAlarmVO> subscribeAlarmVOs = SubscribeAlarmVO.transFromPOs(subscribeAlarmPOs);

			data.put("subscribeAlarmVOs", JSONObject.toJSON(subscribeAlarmVOs));
			data.put("total", subscribeAlarmVOs.size());
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 按关键字模糊查询订阅信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryByKeyword", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySubscribeByKeyword(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		try {

			// Pageable pageable = PageRequest.of(pageIndex, pageSize);

			Pageable pageable = new PageRequest(pageIndex, pageSize);

			Page<SubscribeAlarmPO> subscribeAlarmPOPage = subscribeAlarmDAO.findByKeywordContaining(pageable,
					keyword == null ? "" : keyword);

			List<SubscribeAlarmVO> subscribeAlarmVOs = SubscribeAlarmVO.transFromPOs(subscribeAlarmPOPage.getContent());

			data.put("subscribeAlarmVOs", JSONObject.toJSON(subscribeAlarmVOs));
			data.put("total", subscribeAlarmPOPage.getTotalElements());
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

}