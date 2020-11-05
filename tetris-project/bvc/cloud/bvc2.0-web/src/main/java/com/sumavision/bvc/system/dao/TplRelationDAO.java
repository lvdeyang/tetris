package com.sumavision.bvc.system.dao;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TplContentPO.class, idClass = Long.class)
public interface TplRelationDAO extends MetBaseDAO<TplContentPO>{
	public TplContentPO findByUuid(String uuid);
	
}
