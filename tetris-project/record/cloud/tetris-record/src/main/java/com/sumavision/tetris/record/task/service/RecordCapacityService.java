package com.sumavision.tetris.record.task.service;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.record.device.DeviceDAO;
import com.sumavision.tetris.record.device.DevicePO;

@Service
public class RecordCapacityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordCapacityService.class);

	@Autowired
	private CapacityService capacityService;

	@Autowired
	private DeviceDAO deviceDAO;

	@Value("${taskTemplateName:RECORD_COMMON}")
	private String templateName;

	public void addRecordTask(RecordTimerBO recordTimerBO) throws Exception {

		DevicePO devicePO;

		try {
			devicePO = deviceDAO.findOne(recordTimerBO.getDeviceId());

			if (devicePO == null) {
				return;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		JSONObject addRecordJson = new JSONObject();

//		JSONObject sourceParamJson = new JSONObject();
//		sourceParamJson.put("url", recordTimerBO.getSourceUrl());
//
//		JSONObject outputParamJson = new JSONObject();
//
//		String filePath = "/" + recordTimerBO.getRecordStrategyName() + "/" + recordTimerBO.getOperateTime().getTime();
//
//		// 录制的绝对路径
//		String absolutePath = recordTimerBO.getRecordBasePath() + filePath;
//
//		outputParamJson.put("name", absolutePath);
//
//		addRecordJson.put("type", recordTimerBO.getSourceType());
//		addRecordJson.put("deviceIp", devicePO.getDeviceIP());
//		addRecordJson.put("sourceParam", sourceParamJson);
//		addRecordJson.put("outputParam", outputParamJson);

		JSONArray inputs = new JSONArray();
		JSONObject input = new JSONObject();
		input.put("type",recordTimerBO.getSourceType());
		input.put("url",recordTimerBO.getSourceUrl());
		inputs.add(input);

		JSONArray outputs = new JSONArray();
		JSONObject output = new JSONObject();
		String filePath = "/" + recordTimerBO.getRecordStrategyName() + "/" + recordTimerBO.getOperateTime().getTime();
		String absolutePath = recordTimerBO.getRecordBasePath() + filePath;
		output.put("url",absolutePath);
		outputs.add(output);

		addRecordJson.put("template",templateName);
		addRecordJson.put("task_ip",devicePO.getDeviceIP());
		addRecordJson.put("map_sources",inputs);
		addRecordJson.put("map_outputs",outputs);

		try {
			LOGGER.info("RecordCapacityService, addRecord, params=" + addRecordJson.toJSONString());

			String result = capacityService.addTaskByTemplate(addRecordJson.toJSONString());
			JSONObject resultObj = JSONObject.parseObject(result);
			recordTimerBO.setCapacityTaskId(resultObj.getString("taskId"));

//			String capacityTaskId = capacityService.addRecord(addRecordJson.toJSONString());
//			recordTimerBO.setCapacityTaskId(capacityTaskId);

			recordTimerBO.setFilePath(filePath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("RecordCapacityService addRecordTask() exception, e=" + e.toString());
			e.printStackTrace();
			throw e;
		}
	}

	public void delRecordTask(String capacityTaskId) throws Exception {
		capacityService.deleteRecord(capacityTaskId);
	}
}
