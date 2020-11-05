package com.sumavision.tetris.record.strategy;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RecordStrategyItemPO.class, idClass = Long.class)
public interface RecordStrategyItemDAO extends BaseDAO<RecordStrategyItemPO> {

	List<RecordStrategyItemPO> findByRecordStrategyId(Long recordStgId);

	List<RecordStrategyItemPO> findByRecordStrategyId(Long recordStgId, Pageable pageRequest);

	List<RecordStrategyItemPO> findByNameAndRecordStrategyId(String name, Long recordStgId);

	List<RecordStrategyItemPO> findByIdAndRecordStrategyIdAndStartTimeAndStopTime(Long id, Long recordStgId, Date startTime,
			Date stopTime);

	@Modifying
	@Transactional
	@Query(value = "delete from RecordStrategyItemPO r where r.recordStrategyId = (?1)")
	void deleteByRecordStrategyId(Long recordStgId);

}
