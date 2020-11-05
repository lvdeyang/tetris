package com.sumavision.tetris.bvc.business.query;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-bvc-business", configuration = FeignConfiguration.class)
public interface QueryFeign {
	
	@RequestMapping(value = "/command/system/query/count/of/transmit", method = RequestMethod.POST)
	public JSONObject queryCountOfTransmit() throws Exception;
	
	@RequestMapping(value = "/command/system/query/count/of/review", method = RequestMethod.POST)
	public JSONObject queryCountOfReview() throws Exception;
	
}
