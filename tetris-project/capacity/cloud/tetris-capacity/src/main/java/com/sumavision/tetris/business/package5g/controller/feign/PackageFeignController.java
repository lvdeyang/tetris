package com.sumavision.tetris.business.package5g.controller.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.business.package5g.service.PackageService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller(value = "/capacity/package/feign")
public class PackageFeignController {
	
	@Autowired
	private PackageService packageService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			String deviceIp,
			String port,
			String dstIp,
			String dstPort,
			HttpServletRequest request) throws Exception{
		
		return packageService.addTask(deviceIp, port, dstIp, dstPort);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(
			String taskId,
			HttpServletRequest request) throws Exception{
		
		packageService.delete(taskId);
		
		return null;
	}
}
