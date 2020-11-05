package com.sumavision.tetris.websocket.core.load.balance;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SessionMetadataPO.class, idClass=Long.class)
public interface SessionMetadataDAO extends BaseDAO<SessionMetadataPO>{

	/**
	 * 查询用户websocket session元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:45:03
	 * @param String userId 用户id
	 * @return SessionMetadataPO websocket session 元数据
	 */
	public SessionMetadataPO findByUserId(String userId);
	
	/**
	 * 根据sessionId查询webscocket session元数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:49:58
	 * @param String sessionId websocket session id
	 * @return SessionMetadataPO websocket session 元数据
	 */
	public SessionMetadataPO findBySessionId(String sessionId);
	
}
