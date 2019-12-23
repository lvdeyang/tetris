package com.sumavision.tetris.sts.device.backup.condition;

import com.sumavision.tetris.sts.common.CommonDao;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = BackupConditionPO.class, idClass = Long.class)
public interface BackupConditionDao extends CommonDao<BackupConditionPO> {

	public BackupConditionPO findTopByIdIsNotNull();
	
}
