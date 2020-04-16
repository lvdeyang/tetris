package com.sumavision.tetris.auth.token.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/token/feign")
public class TokenFeignController {

	@Autowired
	private TokenQuery tokenQuery;
	
	/**
	 * 批量查询用户状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午11:41:08
	 * @param JSONArray userIds 用户id列表
	 * @param String terminalType 终端类型
	 * @return List<TokenVO> 用户状态列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/user/id/in/and/type")
	public Object findByUserIdInAndType(
			String userIds,
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		return tokenQuery.findByUserIdInAndType(JSON.parseArray(userIds, Long.class), TerminalType.valueOf(terminalType));
	}
	
	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @param String terminalType 终端类型
	 * @return boolean 判断结果
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/token")
	public Object checkToken(
			String token, 
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		return tokenQuery.checkToken(token, TerminalType.valueOf(terminalType));
	}
	
}
