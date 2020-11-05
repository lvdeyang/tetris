package com.sumavision.tetris.tool.test.flow.client.core.rmi;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "tetris-test-flow")
public interface SchemeFeign {

	@RequestMapping(value = "/scheme/save")
	public void save(
			@RequestParam("serviceUuid") String serviceUuid,
			@RequestParam("name") String name,
			@RequestParam("className") String className,
			@RequestParam("methodName") String methodName,
			@RequestParam("uri") String uri,
			@RequestParam("param") String param,
			@RequestParam("expect") String expect) throws Exception;
	
}
