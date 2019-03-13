package com.sumavision.tetris.api.terminal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.api.terminal.exception.ImageVerificationCodeErrorException;
import com.sumavision.tetris.api.terminal.exception.ImageVerificationCodeTimeoutException;
import com.sumavision.tetris.api.terminal.exception.MobileVerificationCodeErrorException;
import com.sumavision.tetris.api.terminal.exception.MobileVerificationCodeTimeoutException;
import com.sumavision.tetris.auth.login.LoginService;
import com.sumavision.tetris.commons.util.random.RandomMessage;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.context.HttpSessionContext;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserService;

@Controller
@RequestMapping(value = "/api/terminal/auth")
public class ApiTerminalAuthController {
	
	@Autowired
	private RandomMessage randomMessage;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 移动终端请求手机验证码<br/>
	 * <p>url:/api/terminal/auth/generate/phone/verification/code</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 上午10:09:29
	 * @param String mobile 手机号
	 * @return String sessionToken 有效期三分钟
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/phone/verification/code")
	public Object generatePhoneVerificationCode(
			String mobile,
			HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		
		session.setMaxInactiveInterval(HttpConstant.VERIFICATION_CODE_TIMEOUT);
		
		String number = randomMessage.onlyNumber(6);
		
		//TODO 发短信
		System.out.println(number);
		
		session.setAttribute("verification-code-phone", number);
		
		return session.getId();
	}
	
	/**
	 * 引动终端用户注册<br/>
	 * <p>url:/api/terminal/auth/do/register</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 上午10:26:37
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String passwordRepeat 重复密码
	 * @param String mobile 手机号码
	 * @param String verificationCode 验证码
	 * @param String sessionToken session标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/register")
	public Object doRegister(
			String username,
			String password,
			String passwordRepeat,
			String mobile,
			String verificationCode,
			String sessionToken,
			HttpServletRequest request) throws Exception{
		
		HttpSession session = HttpSessionContext.get(sessionToken);
		if(session == null){
			throw new MobileVerificationCodeTimeoutException(username, mobile);
		}
		
		String number = session.getAttribute("verification-code-phone").toString();
		if(!number.equals(verificationCode)){
			throw new MobileVerificationCodeErrorException(username, mobile);
		}
		
		userService.add(username, username, password, passwordRepeat, mobile, null, UserClassify.TERMINAL.getName(), false);
		
		return null;
	}
	
	/**
	 * 用户名密码登录<br/>
	 * <p>url:/api/terminal/auth/do/username/password/login</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 上午9:43:12
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String verificationCode 验证码
	 * @param String sessionToken session token
	 * @return String token 用户登录token
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/username/password/login")
	public Object doUsernamePasswordLogin(
			String username,
			String password,
			String verificationCode,
			String sessionToken,
			HttpServletRequest request) throws Exception{
		
		/*HttpSession session = HttpSessionContext.get(sessionToken);
		if(session == null){
			throw new ImageVerificationCodeTimeoutException(username);
		}
		
		String existCode = session.getAttribute("verification-code-image").toString();
		if(!existCode.equals(verificationCode)){
			throw new ImageVerificationCodeErrorException(username);
		}*/
		
		String token = loginService.doPasswordLogin(username, password, null);
		
		return token;
	}
	
	public Object doPhonePasswordLogin(
			String mobile,
			String password,
			HttpServletRequest request) throws Exception{
		return null;
	}
	
	public Object doWechatLogin() throws Exception{
		return null;
	}
	
}
