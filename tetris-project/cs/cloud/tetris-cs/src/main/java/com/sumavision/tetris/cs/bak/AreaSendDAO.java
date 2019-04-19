package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AreaSendPO.class, idClass = Long.class)
public interface AreaSendDAO extends BaseDAO<AreaSendPO>{
	@Query(value = "SELECT * FROM TETRIS_CS_SEND_AREA WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<AreaSendPO> findByChannelId(Long reg1);
}
