package com.suma.venus.alarmoprlog.controller.alarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.IRawAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EAlarmLevel;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.service.alarm.AlarmInfoService;
import com.suma.venus.alarmoprlog.service.alarm.AlarmService;
import com.suma.venus.alarmoprlog.service.alarm.vo.QueryAlarmVO;
import com.suma.venus.alarmoprlog.websocket.WebSocketServer;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.alarm.bo.http.AlarmNotifyBO;
// import com.sumavision.tetris.capacity.server.CapacityService;
// import com.sumavision.tetris.resouce.feign.bundle.BundleFeignService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private IAlarmDAO alarmDAO;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private IAlarmInfoDAO alarmInfoDAO;

	@Autowired
	private IRawAlarmDAO rawAlarmDAO;

	@Autowired
	private AlarmInfoService AlarmInfoService;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	// @Autowired
	// private BundleFeignService bundleFeignService;
	
	// @Autowired 
	// private CapacityService capacityService;

	@RequestMapping(value = "/addAlarmPO", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> test() {

		Map<String, Object> data = new HashMap<String, Object>();

		try {

			AlarmInfoPO alarmInfoPO = new AlarmInfoPO();

			alarmInfoPO.setAlarmCode("10111");
			alarmInfoPO.setAlarmLevel(EAlarmLevel.INFO);
			alarmInfoPO.setAlarmName("测试告警2");
			alarmInfoPO.setAlarmSolution("88888");
			alarmInfoDAO.save(alarmInfoPO);

			RawAlarmPO rawAlarmPO = new RawAlarmPO();

			rawAlarmPO.setAlarmInfo(alarmInfoPO);
			rawAlarmPO.setAlarmParams("setxxx");
			rawAlarmPO.setCreateTime(new Date());
			// rawAlarmPO.setSourceIP("10.10.40.26");
			// rawAlarmPO.setSourceObj("testObj");
			rawAlarmPO.setSourceService("testAlarm");
			rawAlarmDAO.save(rawAlarmPO);

			AlarmPO alarmPO = new AlarmPO();

			alarmPO.setAlarmCount(1);
			alarmPO.setAlarmStatus(EAlarmStatus.UNTREATED);
			alarmPO.setLastAlarm(rawAlarmPO);
			alarmPO.setFirstCreateTime(rawAlarmPO.getCreateTime());
			alarmDAO.save(alarmPO);

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	@RequestMapping(value = "/queryAlarmPO", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> testQuery() {

		Map<String, Object> data = new HashMap<String, Object>();

		try {

			SimpleDateFormat sdfmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date firstTime = sdfmat.parse("2018-08-07 16:51:09");
			Date lastTime = sdfmat.parse("2018-08-08 09:20:11");

			QueryAlarmVO queryAlarmVO = new QueryAlarmVO();
			queryAlarmVO.setSourceIP("10.10.40.27");
			// queryAlarmVO.setAlarmLevel(EAlarmLevel.INFO.toString());
			// queryAlarmVO.setAlarmCode("10111");
			queryAlarmVO.setSourceService("testAlarm");
			// queryAlarmVO.setFirstCreateTime(firstTime);
			// queryAlarmVO.setLastCreateTime(lastTime);

			@SuppressWarnings("deprecation")
			Pageable pageable = new PageRequest(0, 2, Sort.Direction.DESC, "id");

			Page<AlarmPO> alarmPOs = alarmService.queryAlarmByQueryVOPage(queryAlarmVO, pageable);

			System.out.println(alarmPOs.getContent().get(0).toString());

			data.put("data", JSONObject.toJSON(alarmPOs));
			data.put("errMsg", "success");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	@RequestMapping(value = "/testNew", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> testNew() {

		Map<String, Object> data = new HashMap<String, Object>();

		File file = new File("D://test.xlsx");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return data;
		}

		try {
			return AlarmInfoService.importAlarmInfoExcel(inputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/push/{cid}")
	public void pushToWeb(@PathVariable String cid, String message) {
		try {
			WebSocketServer.sendInfo(message, cid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/testRestTemplate")
	public String testRestTemplate() {

		AlarmParamBO alarmParamBO = new AlarmParamBO();

		alarmParamBO.setAlarmCode("11010001");
		// alarmParamBO.setSourceObj("obj");
		alarmParamBO.setCreateTime(new Date());

		try {
			restTemplate.postForObject("http://" + "suma-venus-resource" + "/feign/test/testNotify", alarmParamBO,
					String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "true";
	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/testLBRestTemplate")
	public String testLBRestTemplate() {

		AlarmNotifyBO alarmNotifyBO = new AlarmNotifyBO();

		alarmNotifyBO.setAlarmCode("11010001");
		// alarmNotifyBO.setSourceObj("obj");

		try {
			restTemplate.postForObject("http://" + "suma-venus-resource" + "/feign/test/testNotify", alarmNotifyBO,
					String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "true";
	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/testAddDevice")
	public Map<String, String> testAddDevice() {

		Map<String, String> map = null;
		try {
			// map = bundleFeignService.addTransCodeDevice("testchenmo", "10.10.40.27", 5656);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;

	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/testDelDevice")
	public String testDelDevice(String bundle_id) {

		try {
			// bundleFeignService.delTransCodeDevice(bundle_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "true";
	}

	// 推送数据接口
	@ResponseBody
	@RequestMapping("/resetDeviceHeartBeatUrl")
	public String resetDeviceHeartBeatUrl() {

		JSONObject jsObject = new JSONObject();
		jsObject.put("span_ms", 3000);
		jsObject.put("loss_times", 10);
		
		
		JSONObject alarmListObject = new JSONObject();
		alarmListObject.put("ts_plp_high", jsObject);
		
		String str = alarmListObject.toJSONString();
		
		try {
			// capacityService.putAlarmlist("10.10.40.228", str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "true";
	}
	
	// 推送数据接口
	@ResponseBody
	@RequestMapping("/testPutAlarmList")
	public String testPutAlarmList(String bundle_ip) {
		JSONObject jsObject = new JSONObject();
		jsObject.put("span_ms", 3000);
		jsObject.put("loss_times", 30);
		
		
		JSONObject ts_plp_highObject = new JSONObject();
		ts_plp_highObject.put("ts_plp_high", jsObject);
		
		JSONObject alarmListObject = new JSONObject();
		alarmListObject.put("alarm_list", ts_plp_highObject);
		
		
		String str = alarmListObject.toJSONString();
		
		try {
			// capacityService.putAlarmlist(bundle_ip, str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "true";
	}

}
