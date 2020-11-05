package com.sumavision.tetris.mims.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 用户导入事件代理<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:31:38
 */
@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface UserImportFeign {

	/**
	 * 用户注册事件代理<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午4:31:31
	 * @param String users 导入的用户列表
	 */
	@RequestMapping(value = "/event/publish/user/import")
	public JSONObject userImport(@RequestParam("users") String users) throws Exception;
	
}
