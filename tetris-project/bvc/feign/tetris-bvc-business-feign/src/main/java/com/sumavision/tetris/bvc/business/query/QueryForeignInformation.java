/**
 * 
 */
package com.sumavision.tetris.bvc.business.query;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-bvc-business", configuration = FeignConfiguration.class)
public interface QueryForeignInformation {

	@RequestMapping(value="/command/query/find/institution/tree/foreign/bundle", method = RequestMethod.POST)
	public JSONObject stopLiveDevice(
			@RequestParam(value = "folderPath") String folderPath,
			@RequestParam(value = "serNodeNamePath") String serNodeNamePath,
			@RequestParam(value = "permissionType") String permissionType,
			@RequestParam(value = "roleId") Long roleId,
			@RequestParam(value = "childType") String childType,
			@RequestParam(value = "uuid") String uuid) throws Exception;
}
