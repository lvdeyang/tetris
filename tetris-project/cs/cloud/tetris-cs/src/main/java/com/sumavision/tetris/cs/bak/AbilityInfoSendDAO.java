package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = AbilityInfoSendPO.class, idClass = Long.class)
public interface AbilityInfoSendDAO extends BaseDAO<AbilityInfoSendPO>{
	public List<AbilityInfoSendPO> findByChannelId(Long channelId);
	
	@Modifying
	@Query(value = "DELETE FROM TETRIS_CS_SEND_ABILITY_INFO WHERE channel_id = ?1", nativeQuery = true)
	public void deleteByChannelId(Long channelId);
}
