package com.suma.venus.resource.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.LianwangPassbyPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LianwangPassbyPO.class, idClass = Long.class)
public interface LianwangPassbyDAO extends BaseDAO<LianwangPassbyPO>{

	
	
}
