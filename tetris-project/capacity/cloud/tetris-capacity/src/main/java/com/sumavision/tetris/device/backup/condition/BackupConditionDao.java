package com.sumavision.tetris.device.backup.condition;

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = BackupConditionPO.class, idClass = Long.class)
public interface BackupConditionDao extends BaseDAO<BackupConditionPO> {

	public BackupConditionPO findTopByIdIsNotNull();
	
}
