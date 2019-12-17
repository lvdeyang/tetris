package com.sumavision.tetris.auth.token;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/token")
public class TokenController {

	@Autowired
	private TokenDAO tokenDao;
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 查询用户的登录状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 下午4:24:21
	 * @param Long userId 用户id
	 * @return List<TokenVO> 登录状态列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		List<TokenPO> entities = tokenDao.findByUserIdOrderByTypeDesc(userId);
		
		List<TokenVO> tokens = TokenVO.getConverter(TokenVO.class).convert(entities, TokenVO.class);
		
		return tokens;
	}
	
	/**
	 * 作废token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 下午4:34:03
	 * @param Long id token id
	 * @return TokenVO token数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/invalid")
	public Object invalid(
			Long id,
			HttpServletRequest request) throws Exception{
		
		TokenPO entity = tokenService.invalid(id);
		return new TokenVO().set(entity);
	}
	
}
