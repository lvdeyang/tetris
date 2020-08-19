package com.sumavision.tetris.capacity.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
public class PackageService {

	@Autowired
	private CapacityFeign capacityFeign;
	
	public String addTask(
			String deviceIp,
			String port,
			String dstIp,
			String dstPort) throws Exception{
		
		return JsonBodyResponseParser.parseObject(capacityFeign.packageAddTask(deviceIp, port, dstIp, dstPort), String.class);
	}
	
	public void deleteTask(String taskId) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.packageDeleteTask(taskId), null);
	}
	
}
