package com.sumavision.tetris.resouce.feign.resource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface ResourceFeign {

	@RequestMapping(value = "/feign/resource/query/users/by/userId", method = RequestMethod.POST)
	public JSONObject queryUsersByUserId(
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "terminalType") String terminalType) throws Exception;

}
