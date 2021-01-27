package com.sumavision.tetris.record.strategy;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyType;

@RepositoryDefinition(domainClass = RecordStrategyPO.class, idClass = Long.class)
public interface RecordStrategyDAO extends BaseDAO<RecordStrategyPO> {

	@Deprecated
	List<RecordStrategyPO> findBySourceId(Long sourceId);

	@Deprecated
	List<RecordStrategyPO> findBySourceIdAndDelStatusNot(Long sourceId, Integer delStatus);

	@Deprecated
	List<RecordStrategyPO> findBySourceId(Long sourceId, Pageable pageRequest);

	RecordStrategyPO findByIdAndDelStatusNot(Long id, Integer delStatus);

	// 只要求没删除的不能同名，即使同名其实也没啥影响
	List<RecordStrategyPO> findByNameAndSourceIdAndDelStatusNot(String name, String sourceId, Integer delStatus);

	List<RecordStrategyPO> findByTypeAndSourceIdAndDelStatusNot(EStrategyType type, String sourceId, Integer delStatus);

	void deleteBySourceId(Long sourceId);
	
	List<RecordStrategyPO> findByDeviceId(Long deviceId);
	
	List<RecordStrategyPO> findByStorageId(Long storageId);

	List<RecordStrategyPO> findByDeviceIdAndStatus(Long deviceId, RecordStrategyPO.EStrategyStatus status);
	
	List<RecordStrategyPO> findByStorageIdAndStatus(Long storageId, RecordStrategyPO.EStrategyStatus status);
	
}
