package com.sumavision.bvc.device.monitor.record;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=MonitorRecordManyTimesPO.class, idClass=Long.class)
public interface MonitorRecordManyTimesDAO extends MetBaseDAO<MonitorRecordManyTimesPO> {

	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesPO record where record.status='RUN' and record.relationId in ?1")
	public List<MonitorRecordManyTimesPO> findNeedStop(Collection<Long> relationIds);
	
	public List<MonitorRecordManyTimesPO> findByRelationId(Long relationId);
	
	@Query(value="select * from BVC_MONITOR_RECORD_MANY_TIMES where relationId=?1 \n#pageable\n",
			countQuery="select COUNT(ID) from BVC_MONITOR_RECORD_MANY_TIMES where relationId=?1",
			nativeQuery=true)
	public Page<MonitorRecordManyTimesPO> findByRelation(Long relationId, Pageable page);
	
	public MonitorRecordManyTimesPO findByRelationIdAndStatusNot(Long relationId,MonitorRecordStatus status);
}
