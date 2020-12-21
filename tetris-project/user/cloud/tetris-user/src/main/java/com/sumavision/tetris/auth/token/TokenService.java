package com.sumavision.tetris.auth.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TokenService {

	@Autowired
	private TokenDAO tokenDao;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private UserQuery userQuery;
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
		UserPO user = userDAO.findOne(token.getUserId());
		if(TerminalType.QT_ZK.equals(token.getType())){
			UserVO userVO =  userQuery.current();
			//指控终端强制下线校验
			TokenPO tokenqt = tokenDao.findByUserIdAndType(user.getId(), token.getType());
			if(tokenqt!=null && UserStatus.ONLINE.equals(token.getStatus())){
				//强制客户端下线
				websocketMessageService.push(user.getId().toString(), "kickOffLine", null, userVO.getId().toString(), userVO.getNickname());
			}
		}
		token.setStatus(UserStatus.OFFLINE);
		tokenDao.save(token);
		return token;
	}
	
	/**
	 * 作废token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月27日 上午9:12:54
	 * @param TerminalType... types 终端类型
	 */
	public void invalid(TerminalType... types) throws Exception{
		List<TokenPO> tokens = tokenDao.findByTypeInAndStatus(Arrays.asList(types), UserStatus.ONLINE);
		if(tokens!=null && tokens.size()>0){
			List<TokenPO> invalidTokens = new ArrayList<TokenPO>();
			for(TokenPO token:tokens){
				Date now = new Date();
				Date timeScope = DateUtil.addSecond(token.getLastModifyTime(), token.getType().getTokenTimeout());
				if(!timeScope.after(now)){
					token.setToken(null);
					token.setLastModifyTime(null);
					token.setStatus(UserStatus.OFFLINE);
					invalidTokens.add(token);
				}
			}
			if(invalidTokens.size() > 0){
				tokenDao.save(invalidTokens);
			}
		}
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
