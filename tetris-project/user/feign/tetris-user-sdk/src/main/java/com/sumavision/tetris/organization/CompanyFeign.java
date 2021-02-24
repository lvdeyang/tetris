package com.sumavision.tetris.organization;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface CompanyFeign {

	/**
	 * 根据id查询企业<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:54:26
	 * @param Long id 企业id
	 * @return CompanyVO 企业基本信息
	 */
	@RequestMapping(value = "/company/feign/find/by/id")
	public JSONObject findById(@RequestParam("id") Long id) throws Exception;
	
}
