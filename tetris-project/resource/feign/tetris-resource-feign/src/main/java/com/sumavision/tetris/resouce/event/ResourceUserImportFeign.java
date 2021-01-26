package com.sumavision.tetris.resouce.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface ResourceUserImportFeign {

	@RequestMapping(value = "/event/publish/user/import")
	public JSONObject userImport(
			@RequestParam("userId") String userId,
			@RequestParam("nickname") String nickname,
			@RequestParam("userno") String userno,
			@RequestParam("roleIds") String roleIds) throws Exception;
	
}
