package com.sumavision.tetris.auth.token;

import org.springframework.cloud.openfeign.FeignClient;
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
	
	/**
	 * 批量查询用户状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午11:41:08
	 * @param JSONArray userIds 用户id列表
	 * @param String terminalType 终端类型
	 * @return List<TokenVO> 用户状态列表
	 */
	@RequestMapping(value = "/token/feign/find/by/user/id/in/and/type")
	public JSONObject findByUserIdInAndType(@RequestParam("userIds") String userIds, @RequestParam("terminalType") String terminalType);
	
}
