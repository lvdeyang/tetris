package com.sumavision.signal.bvc.director.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.director.po.DirectorSourcePO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = DirectorSourcePO.class, idClass = Long.class)
public interface DirectorSourceDAO extends BaseDAO<DirectorSourcePO>{
	
	public void deleteByTaskIdIn(Collection<String> taskIds);
	
	public List<DirectorSourcePO> findByTaskIdIn(Collection<String> taskIds);

}
