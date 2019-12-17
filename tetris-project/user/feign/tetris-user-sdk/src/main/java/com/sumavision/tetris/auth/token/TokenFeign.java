package com.sumavision.tetris.auth.token;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-user", configuration = FeignConfiguration.class)
public interface TokenFeign {

	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @param String terminalType 终端类型
	 * @return boolean 判断结果
	 */
	@RequestMapping(value = "/token/feign/check/token")
	public JSONObject checkToken(@RequestParam("token") String token, @RequestParam("terminalType") String terminalType);
	
}
