package com.sumavision.tetris.tool.test.flow.client.core.rmi;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "tetris-test-flow")
public interface ServiceFeign {

	/**
	 * @Title: 服务注册<br/> 
	 * @param name 服务名称
	 * @param uuid 服务唯一标识
	 * @param ip 服务ip
	 * @param port 服务端口
	 * @throws Exception
	 */
	@RequestMapping(value = "/service/do/registe")
	public void doRegiste(
			@RequestParam("name") String name, 
			@RequestParam("uuid") String uuid, 
			@RequestParam("ip") String ip, 
			@RequestParam("port") String port) throws Exception;
	
}
