package com.sumavision.tetris.record.file;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;

@RepositoryDefinition(domainClass = RecordFilePO.class, idClass = Long.class)
public interface RecordFileDAO extends BaseDAO<RecordFilePO>{
	
	List<RecordFilePO> findByRecordStrategyIdAndStatus(Long recordStgId, ERecordFileStatus status);
 
	List<RecordFilePO> findByRecordStrategyId(Long recordStgId);

	List<RecordFilePO> findByRecordStrategyIdAndStartTimeAfterAndStopTimeBefore(Long recordStgId, Date start, Date end);

	List<RecordFilePO> findByRecordStrategyIdAndStartTimeAfter(Long recordStgId, Date startTime);

	// List<RecordFilePO> findByDayStr(String dayStr);

	List<RecordFilePO> findByRecordStrategyId(Long recordStgId, Pageable pageRequest);

	@Modifying
	@Transactional
	@Query(value = "delete from RecordFilePO r where r.recordStrategyId = (?1)")
	void deleteByStgyId(Long stgyId);

	// @Modifying
	// @Transactional
	// @Query(value = "delete from RecordFilePO r where r.sourceId = (?1)")
	//void deleteBySourceId(Long sourceId);

	// @Modifying
	// @Transactional
	// @Query(value = "select r.dayStr from RecordFilePO r where r.strategyId = (?1) group by r.dayStr order by r.startTime asc")
	// List<String> listDayStrField(Long stgyId);

	List<RecordFilePO> findByRecordStrategyIdAndStopTimeBefore(Long recordStgId, Date date);
}
