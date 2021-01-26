package com.sumavision.tetris.easy.process.config.server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface EasyProcessServerPropsFeign {

	/**
	 * 查询流程服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 */
	@RequestMapping(value = "/easy/process/server/props/feign/query/props")
	public JSONObject queryProps() throws Exception;
	
}
