package com.sumavision.tetris.omms.software.service.installation;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = BackupInformationPO.class, idClass = Long.class)
public interface BackupInformationDAO extends BaseDAO<BackupInformationPO>{
	
	public List<BackupInformationPO> findByDeploymentId(Long deploymentId);
	
	public List<BackupInformationPO> findByDatabaseBackupIdIn(Collection<Long> backupIds);
}
