package com.sumavision.tetris.websocket.core.load.banlance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface WebsocketServerFeign {
	
	
	@RequestMapping(value = "/websocket/server/addr")
	public JSONObject addr();

}
