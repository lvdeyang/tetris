package com.sumavision.tetris.cs.channel.broad.ability;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = BroadAbilityRemotePO.class, idClass = Long.class)
public interface BroadAbilityRemoteDAO extends BaseDAO<BroadAbilityRemotePO>{
	public BroadAbilityRemotePO findByProcessInstanceId(String processInstanceId);
	
	public BroadAbilityRemotePO findByChannelId(Long channelId);
	
	@Modifying
	@Query(value = "DELETE FROM TETRIS_CS_ABILITY_REMOTE_INFO WHERE channel_id = ?1", nativeQuery = true)
	public void deleteByChannelId(Long channelId);
}
