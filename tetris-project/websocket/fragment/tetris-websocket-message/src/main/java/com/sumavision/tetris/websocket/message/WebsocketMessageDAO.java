package com.sumavision.tetris.websocket.message;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = WebsocketMessagePO.class, idClass = Long.class)
public interface WebsocketMessageDAO extends BaseDAO<WebsocketMessagePO>{

	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:15:02
	 * @param Long userId 用户id
	 * @param boolean consumed 消息是否被消费
	 * @return List<MessagePO> 消息列表
	 */
	public List<WebsocketMessagePO> findByUserIdAndConsumed(Long userId, boolean consumed);
	
}
