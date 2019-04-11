package com.sumavision.tetris.cs.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CsResourcePO.class, idClass = Long.class)
public interface CsResourceDAO extends BaseDAO<CsResourcePO>{
	@Query(value = "SELECT * FROM TETRIS_CS_MENU_RESOURCE WHERE PARENT_ID LIKE ?1", nativeQuery = true)
	public List<CsResourcePO> findResourceFromMenu(Long id);
	
	@Query(value = "SELECT * FROM TETRIS_CS_MENU_RESOURCE WHERE MIMS_ID LIKE ?1", nativeQuery = true)
	public List<CsResourcePO> findResourceByMimsId(Long id);
	
	@Query(value = "SELECT * FROM TETRIS_CS_MENU_RESOURCE WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<CsResourcePO> findResourceByChannelId(Long id);
	
	@Query(value = "SELECT * FROM TETRIS_CS_MENU_RESOURCE WHERE CHANNEL_ID LIKE ?1 AND MIMS_ID LIKE ?2",nativeQuery = true)
	public List<CsResourcePO> findResourceByChannelAndMims(Long channelId,Long mimsId);
}
