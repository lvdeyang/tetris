/**
 * 
 */
package com.sumavision.tetris.user;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月3日 上午9:43:08
 */
@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface GetLoginPageFeign {
	@RequestMapping(value="/getLoginPage/feign/loginPage/get")
	public JSONObject getLoginPage();
}
