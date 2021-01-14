package com.sumavision.tetris.omms.hardware.database.databases;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;


@RepositoryDefinition(domainClass = DatabasesPO.class, idClass = Long.class)
public interface DatabasesDAO extends BaseDAO<DatabasesPO>{
	
	public List<DatabasesPO> findByDatabaseId(Long databaseId);
	
	public List<DatabasesPO> findByIdIn(Collection<Long> ids);

}
