package com.sumavision.tetris.resouce.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface ResourceTouristFeign {

	@RequestMapping(value = "/event/publish/tourist/create")
	public JSONObject touristCreate(
			@RequestParam("userId") String userId,
			@RequestParam("nickname") String nickname,
			@RequestParam("userno") String userno) throws Exception;
	
	@RequestMapping(value = "/event/publish/tourist/delete")
	public JSONObject touristDelete(@RequestParam("userId") String userId) throws Exception;
	
	@RequestMapping(value = "/event/publish/tourist/delete/batch")
	public JSONObject touristDeleteBatch(@RequestParam("userIds") String userIds) throws Exception;
	
}
