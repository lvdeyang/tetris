package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = ChannelNamePO.class, idClass = long.class)
public interface ChannelNameDAO extends MetBaseDAO<ChannelNamePO>{
	
	public List<ChannelNamePO> findByName(String name);
	
}
