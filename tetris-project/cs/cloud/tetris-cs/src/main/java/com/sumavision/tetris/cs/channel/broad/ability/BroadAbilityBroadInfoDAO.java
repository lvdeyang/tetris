package com.sumavision.tetris.cs.channel.broad.ability;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = BroadAbilityBroadInfoPO.class, idClass = Long.class)
public interface BroadAbilityBroadInfoDAO extends BaseDAO<BroadAbilityBroadInfoPO>{
	public List<BroadAbilityBroadInfoPO> findByChannelId(Long channelId);
	
	@Query(value = "SELECT * FROM TETRIS_CS_ABILITY_BROAD_INFO WHERE channel_id <> ?1", nativeQuery = true)
	public List<BroadAbilityBroadInfoPO> findAllExceptChannelId(Long channelId);
	
	public List<BroadAbilityBroadInfoPO> findByUserIdIn(List<Long> userIds);

	@Query(value = "SELECT * FROM TETRIS_CS_ABILITY_BROAD_INFO WHERE user_id in ?1 AND channel_id <> ?2", nativeQuery = true)
	public List<BroadAbilityBroadInfoPO> findByUserIdInExceptChannelId(List<Long> userIds, Long channelId);
	
	@Modifying
	@Query(value = "DELETE FROM TETRIS_CS_ABILITY_BROAD_INFO WHERE channel_id = ?1", nativeQuery = true)
	public void deleteByChannelId(Long channelId);
	
	
	@Query(value = "SELECT preview_url_port FROM TETRIS_CS_ABILITY_BROAD_INFO WHERE preview_url_ip = ?1", nativeQuery = true)
	public List<String> findByPreviewUrlIp(String ip);
	
	public BroadAbilityBroadInfoPO findByUserId(Long userId);
}
