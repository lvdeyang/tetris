package com.sumavision.tetris.cs.channel;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = BroadAbilityBroadInfoPO.class, idClass = Long.class)
public interface BroadAbilityBroadInfoDAO extends BaseDAO<BroadAbilityBroadInfoPO>{
	public List<BroadAbilityBroadInfoPO> findByChannelId(Long channelId);
	
	@Query(value = "SELECT * FROM TETRIS_CS_ABILITY_BROAD_INFO WHERE channel_id <> ?1)", nativeQuery = true)
	public List<BroadAbilityBroadInfoPO> findAllExceptChannelId(Long channelId);
}
