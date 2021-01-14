package com.sumavision.tetris.omms.hardware.database.databaseBackup;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = DatabaseBackupPO.class, idClass = Long.class)
public interface DatabaseBackupDAO extends BaseDAO<DatabaseBackupPO>{
	
	public List<DatabaseBackupPO> findByDatabaseId(Long databaseId);

}
