package com.sumavision.tetris.resouce.feign.bundle;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface BundleFeign {

	/**
	 * 查询转码设备<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryTranscodeDevice", method = RequestMethod.POST)
	public String queryTranscodeDevice() throws Exception;
	
	/**
	 * 查询转码设备<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryDeviceByBundleId", method = RequestMethod.POST)
	public String queryDeviceByBundleId(@RequestParam(value = "bundle_id") String bundle_id) throws Exception;


	/**
	 * 查询设备的授权<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryAuth", method = RequestMethod.POST)
	public JSONObject queryAuth(@RequestParam(value = "bundle_id")String bundle_id) throws Exception;

}
