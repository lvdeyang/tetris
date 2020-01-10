package com.sumavision.tetris.resouce.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "suma-venus-resource", configuration = FeignConfiguration.class)
public interface ResourceUserRegisteredFeign {

	@RequestMapping(value = "/suma-venus-resource/event/publish/user/registered")
	public JSONObject userRegistered(
			@RequestParam("userId") String userId,
			@RequestParam("nickname") String nickname,
			@RequestParam("userno") String userno) throws Exception;
	
}