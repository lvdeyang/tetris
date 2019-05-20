package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = VersionSendPO.class, idClass = Long.class)
public interface VersionSendDAO extends BaseDAO<VersionSendPO>{
	@Query(value = "SELECT * FROM TETRIS_CS_SEND_VERSION WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<VersionSendPO> findByChannelId(Long reg1);
	
	public VersionSendPO findByBroadId(String broadId);
	
	public VersionSendPO findByChannelIdAndBroadId(Long channelId,String broadId);
}
