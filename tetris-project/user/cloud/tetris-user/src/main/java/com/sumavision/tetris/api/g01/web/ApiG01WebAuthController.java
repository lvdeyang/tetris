package com.sumavision.tetris.api.g01.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.auth.login.LoginService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/g01/web/auth")
public class ApiG01WebAuthController {

	/** G01网页端 */
	private static final TerminalType TERMINAL_TYPE = TerminalType.PC_PLATFORM;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	
	
	/**
	 * 用户名密码登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午9:15:34
	 * @param String username 用户名
	 * @param String password 密码
	 * @return token
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/username/password/login")
	public Object doUsernamePasswordLogin(
			String username,
			String password,
			HttpServletRequest request) throws Exception{
		
		String ip = request.getHeader(HttpConstant.HEADER_REAL_IP_FROM_ZUUL);
		if(ip == null) ip = request.getRemoteHost();
		String token = loginService.doPasswordLogin(username, password, ip, TERMINAL_TYPE, null);
		
		return token;
	}
	
	public Object checkToken() throws Exception{
		return null;
	}
	
}
