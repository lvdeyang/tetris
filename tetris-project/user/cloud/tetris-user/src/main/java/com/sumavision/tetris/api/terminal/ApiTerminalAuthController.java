package com.sumavision.tetris.api.terminal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.api.exception.MobileNotMatchedException;
import com.sumavision.tetris.api.exception.MobileVerificationCodeErrorException;
import com.sumavision.tetris.api.exception.MobileVerificationCodeTimeoutException;
import com.sumavision.tetris.auth.login.LoginService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.random.RandomMessage;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.lib.aliyun.push.AliSendSmsService;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.context.HttpSessionContext;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;

@Controller
@RequestMapping(value = "/api/terminal/auth")
public class ApiTerminalAuthController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApiTerminalAuthController.class);
	
	/** 安卓端门户 */
	private static final TerminalType TERMINAL_TYPE = TerminalType.ANDROID_PORTAL;
	
	@Autowired
	private RandomMessage randomMessage;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AliSendSmsService aliSendSmsService;
	
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
		LOG.info(new StringBufferWrapper().append("向手机：").append(mobile).append("发送验证码：").append(number).toString());
		JSONObject param = new JSONObject();
		param.put("code", number);
		
		//TODO: 配aliyun
		aliSendSmsService.sendSms(mobile, param.toJSONString());
		
		session.setAttribute("verification-code-phone", number);
		session.setAttribute("verification-phone", mobile);
		
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
		
		String phone = session.getAttribute("verification-phone").toString();
		if(!phone.equals(mobile)){
			throw new MobileNotMatchedException(mobile, phone);
		}
		
		userService.add(username, username, "", password, passwordRepeat, mobile, null, null, UserClassify.TERMINAL.getName(), false);
		
		return null;
	}
	
	/**
	 * 修改密码<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月16日 上午11:37:15
	 * @param username 用户民
	 * @param mobile 手机号
	 * @param password 密码
	 * @param passwordRepeat 密码重复
	 * @param verificationCode 验证码
	 * @param sessionToken session标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/modify/passward")
	public Object modifyPassward(
			String username,
			String mobile,
			String password,
			String passwordRepeat,
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
		
		String phone = session.getAttribute("verification-phone").toString();
		if(!phone.equals(mobile)){
			throw new MobileNotMatchedException(mobile, phone);
		}
		
		userService.modifyPassword(username, mobile, password, passwordRepeat);
		
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
		String ip = request.getHeader(HttpConstant.HEADER_REAL_IP_FROM_ZUUL);
		if(ip == null) ip = request.getRemoteHost();
		String token = loginService.doPasswordLogin(username, password, ip, TERMINAL_TYPE, null);
		
		return token;
	}
	
	/**
	 * 手机验证码登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:12
	 * @param String mobile 手机号
	 * @param String phoneVerifyCode 手机验证码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/phone/login")
	public Object doPhonePasswordLogin(
			String mobile,
			String password,
			HttpServletRequest request) throws Exception{
		return null;
	}
	
	/**
	 * 微信登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:15
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/wechat/login")
	public Object doWechatLogin(HttpServletRequest request) throws Exception{
		return null;
	}
	
	/**
	 * 查询用户信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午9:37:32
	 * @return UserVO 当前用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/info")
	public Object queryUserInfo(HttpServletRequest request) throws Exception{
		
		return userQuery.current();
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
