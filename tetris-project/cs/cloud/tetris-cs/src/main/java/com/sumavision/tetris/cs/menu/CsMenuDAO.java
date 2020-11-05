package com.sumavision.tetris.cs.menu;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CsMenuPO.class, idClass = Long.class)
public interface CsMenuDAO extends BaseDAO<CsMenuPO> {

	public List<CsMenuPO> findByChannelId(Long reg1);

	public List<CsMenuPO> findByParentId(Long parentId);

	public List<CsMenuPO> findByChannelIdAndParentId(Long channelId, Long parentId);
	
	public CsMenuPO findByChannelIdAndParentIdAndName(Long channelId, Long parentId, String name);
}
