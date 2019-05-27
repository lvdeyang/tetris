package com.sumavision.tetris.cs.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CsMenuPO.class, idClass = Long.class)
public interface CsMenuDAO extends BaseDAO<CsMenuPO> {

	@Query(value = "SELECT * FROM TETRIS_CS_CHANNEL_MENU WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<CsMenuPO> findByChannelId(Long reg1);

	public List<CsMenuPO> findByParentId(Long parentId);

	public List<CsMenuPO> findByChannelIdAndParentId(Long channelId, Long parentId);
}
