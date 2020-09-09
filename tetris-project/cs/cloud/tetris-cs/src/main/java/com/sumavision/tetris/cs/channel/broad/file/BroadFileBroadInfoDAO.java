package com.sumavision.tetris.cs.channel.broad.file;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = BroadFileBroadInfoPO.class, idClass = Long.class)
public interface BroadFileBroadInfoDAO extends BaseDAO<BroadFileBroadInfoPO>{
	public List<BroadFileBroadInfoPO> findByChannelId(Long channelId);
	
	public List<BroadFileBroadInfoPO> findByUserIdIn(List<Long> userIds);
	
	@Query(value = "SELECT * FROM TETRIS_CS_FILE_BROAD_INFO WHERE user_id in ?1 AND channel_id <> ?2", nativeQuery = true)
	public List<BroadFileBroadInfoPO> findByUserIdInExceptChannelId(List<Long> userIds, Long channelId);
	
	@Modifying
	@Query(value = "DELETE FROM TETRIS_CS_FILE_BROAD_INFO WHERE channel_id = ?1", nativeQuery = true)
	public void deleteByChannelId(Long channelId);
}
