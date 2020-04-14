package com.sumavision.tetris.auth.token;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.exception.TokenTimeoutException;
import com.sumavision.tetris.user.exception.TokenUpdatedException;

@Component
public class TokenQuery {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenQuery.class);
	
	@Autowired
	private TokenDAO tokenDao;

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
		TokenPO entity = tokenDao.findByTokenAndType(token, type);
		if(entity == null){
			LOG.error(new StringBufferWrapper().append("token 无效：").append(token).append("，终端类型：").append(type.getName()).toString());
			throw new TokenUpdatedException();
		}
		return checkToken(entity);
	}
	
	/**
	 * token校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午9:13:27
	 * @param String token token对象
	 * @return boolean 检查结果
	 */
	public boolean checkToken(TokenPO token) throws Exception{
		if(token == null){
			throw new TokenUpdatedException();
		}
		if(token.getToken() == null){
			throw new TokenTimeoutException();
		}
		if(token.getType().getTokenTimeout() == 0){
			return true;
		}
		Date now = new Date();
		Date timeScope = DateUtil.addSecond(token.getLastModifyTime(), token.getType().getTokenTimeout());
		if(!timeScope.after(now)){
			LOG.error(new StringBufferWrapper().append("token 超时：").append(token.getToken()).append("，终端类型：").append(token.getType().getName()).toString());
			throw new TokenTimeoutException();
		}
		token.setLastModifyTime(now);
		token.setStatus(UserStatus.ONLINE);
		tokenDao.save(token);
		return true;
	}
	
}
