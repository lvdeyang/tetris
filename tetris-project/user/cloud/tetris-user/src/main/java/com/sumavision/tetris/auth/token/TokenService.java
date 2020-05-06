package com.sumavision.tetris.auth.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserStatus;

@Service
@Transactional(rollbackFor = Exception.class)
public class TokenService {

	@Autowired
	private TokenDAO tokenDao;
	
	/**
	 * 作废token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 下午4:37:06
	 * @param Long id token id
	 * @return TokenPO token数据
	 */
	public TokenPO invalid(Long id) throws Exception{
		TokenPO token = tokenDao.findOne(id);
		token.setToken(null);
		token.setLastModifyTime(null);
		token.setStatus(UserStatus.OFFLINE);
		tokenDao.save(token);
		return token;
	}
	
	/**
	 * token上线<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月30日 下午3:38:59
	 * @param String token token
	 * @return TokenPO token数据
	 */
	public TokenPO online(String token) throws Exception{
		TokenPO entity = tokenDao.findByToken(token);
		if(entity != null){
			entity.setStatus(UserStatus.ONLINE);
			tokenDao.save(entity);
		}
		return entity;
	}
	
}
