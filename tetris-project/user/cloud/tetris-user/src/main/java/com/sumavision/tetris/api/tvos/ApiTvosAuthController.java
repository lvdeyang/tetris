package com.sumavision.tetris.api.tvos;

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
@RequestMapping(value = "/api/tvos/auth")
public class ApiTvosAuthController {

	/** 安卓机顶盒*/
	private static final TerminalType TERMINAL_TYPE = TerminalType.ANDROID_TVOS;
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 用户名密码登录<br/>
	 * <p>url:/api/tvos/auth/do/username/password/login</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 上午9:43:12
	 * @param String username 用户名
	 * @param String password 密码
	 * @return String token 用户登录token
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
	
	/**
	 * 用户注销登录<br/>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月8日 上午10:37:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/logout")
	public Object doLogout(HttpServletRequest request) throws Exception{
		loginService.doLogout();
		return null;
	}
	
}
