package com.sumavision.tetris.mims.app.operation.statistic;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageStatus;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OperationStatisticStrategyPO.class, idClass = Long.class)
public interface OperationStatisticStrategyDAO extends BaseDAO<OperationStatisticStrategyPO>{
	public List<OperationStatisticStrategyPO> findByGroupId(String groupId);
	
	public List<OperationStatisticStrategyPO> findByGroupIdAndStatus(String groupId, OperationPackageStatus status);
}
