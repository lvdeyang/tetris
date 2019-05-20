package com.sumavision.tetris.cs.area;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AreaPO.class, idClass = Long.class)
public interface AreaDAO extends BaseDAO<AreaPO>{
	@Query(value = "SELECT * FROM TETRIS_CS_AREA WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<AreaPO> findByChannelId(Long reg1);
	
	public AreaPO findByChannelIdAndAreaId(Long channelId,String areaId);
}
