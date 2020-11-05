package com.sumavision.tetris.zoom.contacts;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ContactsMessagePO.class, idClass = Long.class)
public interface ContactsMessageDAO extends BaseDAO<ContactsMessagePO>{

	/**
	 * 根据消息发送者和消息发送目标查询消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午2:02:19
	 * @param String fromUserId 消息发送用户id
	 * @param String toUserId 消息发送目标用户id
	 * @return List<ContactsMessagePO> 消息列表
	 */
	public List<ContactsMessagePO> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
	
}
