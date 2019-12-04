package com.sumavision.tetris.capacity.server;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-capacity", configuration = FeignConfiguration.class)
public interface CapacityFeign {

	/**
	 * 添加收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:40:40
	 * @param String recordInfo 收录信息
	 * @return String 收录标识
	 */
	@RequestMapping(value = "/capacity/record/feign/add")
	public JSONObject add(@RequestParam("recordInfo") String recordInfo) throws Exception;
	
	/**
	 * 停止收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:42:22
	 * @param String id 收录标识
	 */
	@RequestMapping(value = "/capacity/record/feign/delete")
	public JSONObject delete(@RequestParam("id") String id) throws Exception;
	
}
