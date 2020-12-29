package com.sumavision.bvc.device.monitor.record;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition( domainClass=MonitorRecordManyTimesRelationPO.class , idClass=Long.class)
public interface MonitorRecordManyTimesRelationDAO extends MetBaseDAO<MonitorRecordManyTimesRelationPO> {
	
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationPO relation where relation.status='WAITING' and relation.nextStartTime<=?1")
	public List<MonitorRecordManyTimesRelationPO> findNeedUpdateTime(Date time); 
	
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationPO relation where relation.status='WAITING' and relation.nextEndTime<=?1")
	public List<MonitorRecordManyTimesRelationPO> findNeedStopRecord(Date time);
	
	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationPO relation where relation.status='WAITING' and relation.nextStartTime<=?1")
	public List<MonitorRecordManyTimesRelationPO> findMissedRelations(Date time);

	public MonitorRecordManyTimesRelationPO findByBusinessId(Long businessId);
}
