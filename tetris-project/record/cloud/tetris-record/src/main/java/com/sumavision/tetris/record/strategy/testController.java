package com.sumavision.tetris.record.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.capacity.server.CapacityService;

@Controller
@RequestMapping(value = "/record/test")
public class testController {

	@Autowired
	private CapacityService capacityService;

	@ResponseBody
	@RequestMapping(value = "/addRecord")
	public Map<String, Object> addRecord(@RequestParam String path, @RequestParam String type,
			@RequestParam String url) {

		Map<String, Object> data = new HashMap<String, Object>();

		JSONObject addRecordJson = new JSONObject();

		JSONObject sourceParamJson = new JSONObject();
		sourceParamJson.put("url", url);

		JSONObject outputParamJson = new JSONObject();
		outputParamJson.put("name", "/home/record" + path);

		addRecordJson.put("type", type);
		addRecordJson.put("deviceIp", "10.10.40.116");
		addRecordJson.put("sourceParam", sourceParamJson);
		addRecordJson.put("outputParam", outputParamJson);

		System.out.println(addRecordJson.toJSONString());

		try {

			String result = capacityService.addRecord(addRecordJson.toJSONString());
			System.out.println("result= " + result);

			data.put("taskId", result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data.put("errMsg", e.toString());
		}

		return data;
	}

	@ResponseBody
	@RequestMapping(value = "/delRecord")
	public Map<String, Object> delRecord(@RequestParam String id) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			System.out.println("delrecord id=" + id);
			capacityService.deleteRecord(id);
		} catch (Exception e) {
			data.put("errMsg", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

}
