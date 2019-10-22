package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AbilityInfoSendPO.class, idClass = Long.class)
public interface AbilityInfoSendDAO extends BaseDAO<AbilityInfoSendPO>{
	public AbilityInfoSendPO findByChannelId(Long channelId);
	
	@Query(value = "SELECT broad_id FROM TETRIS_CS_SEND_ABILITY_INFO WHERE broad_id is not null", nativeQuery = true)
	public List<Integer> getAbilityBroadIds();
}
