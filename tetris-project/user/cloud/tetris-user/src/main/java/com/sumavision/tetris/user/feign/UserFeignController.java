package com.sumavision.tetris.user.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/user/feign")
public class UserFeignController {

	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @return boolean 判断结果
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/token")
	public Object checkToken(
			String token, 
			HttpServletRequest request) throws Exception{
		
		return userQuery.checkToken(token);
	}
	
	/**
	 * 查询当前登录用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 上午10:23:32
	 * @return UserVO 当前登录用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/current")
	public Object current(HttpServletRequest request) throws Exception{
		
		return userQuery.current();
	}
	
	/**
	 * 根据token查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午6:23:15
	 * @param String token 用户登录token
	 * @return UserVO 用户数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/token")
	public Object findByToken(
			String token,
			HttpServletRequest request) throws Exception{
		
		return userQuery.findByToken(token);
	}
	
}
