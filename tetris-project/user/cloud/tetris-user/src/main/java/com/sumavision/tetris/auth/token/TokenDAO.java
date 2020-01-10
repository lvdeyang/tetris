package com.sumavision.tetris.auth.token;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TokenPO.class, idClass = Long.class)
public interface TokenDAO extends BaseDAO<TokenPO>{

	/**
	 * 查询用户的登录状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 下午4:21:22
	 * @param Long userId 用户id
	 * @return List<TokenPO> 登录状态列表
	 */
	public List<TokenPO> findByUserIdOrderByTypeDesc(Long userId);
	
	/**
	 * 获取用户特定终端类型登录的token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月5日 下午5:43:25
	 * @param Long userId 用户id
	 * @param TerminalType type 终端类型
	 * @return TokenPO token对象
	 */
	public TokenPO findByUserIdAndType(Long userId, TerminalType type);
	
	public List<TokenPO> findByUserIdInAndType(Collection<Long> userIds, TerminalType type);
	
	/**
	 * 根据token代码和终端类型查询token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午9:08:33
	 * @param String token token代码
	 * @param TerminalType type 终端类型
	 * @return TokenPO token对象
	 */
	public TokenPO findByTokenAndType(String token, TerminalType type);
	
	/**
	 * 根据token代码查询token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午10:27:52
	 * @param String token token代码
	 * @return TokenPO token对象
	 */
	public TokenPO findByToken(String token);
	
}
