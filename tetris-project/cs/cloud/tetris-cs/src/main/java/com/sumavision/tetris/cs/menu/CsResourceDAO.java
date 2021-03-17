package com.sumavision.tetris.cs.menu;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CsResourcePO.class, idClass = Long.class)
public interface CsResourceDAO extends BaseDAO<CsResourcePO>{
	
	public List<CsResourcePO> findByMimsUuid(String uuid);
	
	public List<CsResourcePO> findByChannelId(Long id);
	
	public List<CsResourcePO> findResourceByChannelIdAndMimsUuid(Long channelId,String mimsUuid);
	
	public List<CsResourcePO> findByParentId(Long menuId);
	
	public List<CsResourcePO> findByNameAndChannelId(String name,Long channelId);
}