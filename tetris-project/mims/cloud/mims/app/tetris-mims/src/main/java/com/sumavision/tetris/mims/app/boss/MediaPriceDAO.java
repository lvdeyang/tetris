package com.sumavision.tetris.mims.app.boss;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaPricePO.class, idClass = Long.class)
public interface MediaPriceDAO extends BaseDAO<MediaPricePO>{

	public List<MediaPricePO> findByMediaIdAndMediaType(Long mediaId,MediaType mediaType);
	
	public List<MediaPricePO> findByMediaIdIn(Collection<Long> mediaIds);
}
