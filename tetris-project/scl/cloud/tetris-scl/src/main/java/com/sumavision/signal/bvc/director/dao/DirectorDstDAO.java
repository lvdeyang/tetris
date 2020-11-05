package com.sumavision.signal.bvc.director.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.director.po.DirectorDstPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = DirectorDstPO.class, idClass = Long.class)
public interface DirectorDstDAO extends BaseDAO<DirectorDstPO>{
	
	public void deleteByTaskIdIn(Collection<String> taskIds);
	
	public List<DirectorDstPO> findByTaskIdIn(Collection<String> taskIds);

}
