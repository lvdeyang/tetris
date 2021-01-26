package com.sumavision.tetris.resouce.feign.bundle._5g;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface FifthGenerationKnapsackFeign {

	/**
	 * 5G背包注册<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午2:35:16
	 * @param String bundleId 5G背包序列号
	 * @return String status 状态
	 * @return String message 信息
	 * @return Integer data 端口
	 */
	@RequestMapping(value = "/fifth/generation/knapsack/do/register", method = RequestMethod.POST)
	public JSONObject doRegister(@RequestParam(value = "bundleId") String bundleId) throws Exception;
	
	/**
	 * 5G背包注销<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午2:55:16
	 * @param String bundleIds 5G背包序列号列表,逗号分隔
	 * @return String ERRMSG 有错误的时候会返回这个
	 */
	@RequestMapping(value = "/bundle/delete", method = RequestMethod.POST)
	public JSONObject doUnregister(@RequestParam(value = "bundleIds") String bundleIds) throws Exception;
	
}
