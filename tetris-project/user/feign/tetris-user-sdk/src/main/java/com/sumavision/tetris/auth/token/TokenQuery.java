package com.sumavision.tetris.auth.token;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class TokenQuery {
	
	@Autowired
	private TokenFeign tokenFeign;

	/**
	 * 批量查询用户状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午11:41:08
	 * @param Collection<Long> userIds 用户id列表
	 * @param TerminalType type 终端类型
	 * @return List<TokenVO> 用户状态列表
	 */
	public List<TokenVO> findByUserIdInAndType(Collection<Long> userIds, TerminalType type) throws Exception{
		return JsonBodyResponseParser.parseArray(tokenFeign.findByUserIdInAndType(JSON.toJSONString(userIds), type.toString()), TokenVO.class);
	}
	
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
