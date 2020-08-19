package com.sumavision.tetris.cs.schedule;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SchedulePO.class, idClass = Long.class)
public interface ScheduleDAO extends BaseDAO<SchedulePO>{
	
	@Query(value = "SELECT * FROM TETRIS_CS_SCHEDULE WHERE channel_id = ?1 ORDER BY broad_date \n#pageable\n",
			countQuery = "SELECT COUNT(DISTINCT id)  FROM TETRIS_CS_SCHEDULE WHERE channel_id = ?1 ORDER BY broad_date",
			nativeQuery = true)
	public Page<SchedulePO> findByChannelId(Long channelId, org.springframework.data.domain.Pageable page);
	
	@Query(value = "SELECT * FROM TETRIS_CS_SCHEDULE WHERE channel_id = ?1 ORDER BY broad_date", nativeQuery = true)
	public List<SchedulePO> findByChannelId(Long channelId);
	
	public SchedulePO findByBroadDate(String broadDate);
	
	public SchedulePO findByBroadDateAndIdNot(String broadDate, Long id);
}
