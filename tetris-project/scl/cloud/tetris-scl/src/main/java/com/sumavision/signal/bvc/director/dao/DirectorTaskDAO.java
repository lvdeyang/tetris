package com.sumavision.signal.bvc.director.dao;

import java.util.Collection;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.director.po.DirectorTaskPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = DirectorTaskPO.class, idClass = Long.class)
public interface DirectorTaskDAO extends BaseDAO<DirectorTaskPO>{

	public void deleteByTaskIdIn(Collection<String> taskIds);
	
}
