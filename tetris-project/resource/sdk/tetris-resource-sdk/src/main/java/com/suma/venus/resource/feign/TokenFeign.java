package com.suma.venus.resource.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.suma.venus.resource.base.bo.AccessToken;
import com.suma.venus.resource.base.bo.ResultBO;

/**
 * token相关接口
 * @author lxw
 *
 */
@FeignClient(name="bvc-service-auth")
@RequestMapping("/oauth")
public interface TokenFeign {

	/**
	 * 获取token
	 * @param authorization
	 * @param username
	 * @param password(MD5编码)
	 * @param grant_type
	 * @return
	 */
	@RequestMapping(value="/token",method=RequestMethod.POST)
	public AccessToken getToken(@RequestHeader(value="Authorization")String authorization,@RequestParam(value="username")String username,
			@RequestParam(value="password")String password,@RequestParam(value="grant_type")String grant_type);
	
	/**
	 * 清除token
	 * @param authorization
	 * @param access_token
	 * @return
	 */
	@RequestMapping(value="/token",method=RequestMethod.DELETE)
	public ResultBO clearToken(@RequestHeader(value="Authorization")String authorization,@RequestHeader(value="access_token")String access_token);
	
}
