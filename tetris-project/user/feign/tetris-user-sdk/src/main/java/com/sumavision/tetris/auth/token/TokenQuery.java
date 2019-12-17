package com.sumavision.tetris.auth.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class TokenQuery {
	
	@Autowired
	private TokenFeign tokenFeign;

	/**
	 * token校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午9:11:39
	 * @param String token token代码
	 * @param TerminalType type 终端类型
	 * @return boolean 检查结果
	 */
	public boolean checkToken(String token, TerminalType type) throws Exception{
		return JsonBodyResponseParser.parseObject(tokenFeign.checkToken(token, type.toString()), boolean.class);
	}
	
}
