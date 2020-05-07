package com.sumavision.tetris.mims.app.media.recommend.statistics;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaRecommendStatisticsPO.class, idClass = Long.class)
public interface MediaRecommendStatisticsDAO extends BaseDAO<MediaRecommendStatisticsPO>{
	public MediaRecommendStatisticsPO findByDateAndUserId(String date, Long userId);
	
	public List<MediaRecommendStatisticsPO> findByGroupIdOrderByDateAsc(String groupId);
	
	public List<MediaRecommendStatisticsPO> findByUserIdIn(List<Long> userIds);
	
	public List<MediaRecommendStatisticsPO> findByUserId(Long userId);
}
