package com.sumavision.tetris.resouce.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface ResourceUserRegisteredFeign {

	@RequestMapping(value = "/event/publish/user/registered")
	public JSONObject userRegistered(
			@RequestParam("userId") String userId,
			@RequestParam("nickname") String nickname,
			@RequestParam("userno") String userno) throws Exception;
	
	@RequestMapping(value = "/event/publish/user/offline")
	public JSONObject userOffline(
			@RequestParam("userId") Long userId) throws Exception;
	
	@RequestMapping(value = "/event/publish/user/online")
	public JSONObject userOnline(
			@RequestParam("userId") Long userId) throws Exception;
	
}