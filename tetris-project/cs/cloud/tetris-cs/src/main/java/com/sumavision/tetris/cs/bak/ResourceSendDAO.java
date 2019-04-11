package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ResourceSendPO.class, idClass = Long.class)
public interface ResourceSendDAO extends BaseDAO<ResourceSendPO>{
	@Query(value = "SELECT * FROM TETRIS_CS_SEND_RESOURCE WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<ResourceSendPO> findByChannelId(Long reg1);
}
