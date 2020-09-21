package com.sumavision.bvc.device.monitor.record;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=MonitorRecordManyTimesPO.class, idClass=Long.class)
public interface MonitorRecordManyTimesDAO extends MetBaseDAO<MonitorRecordManyTimesPO> {

	@Query("from com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesPO record where record.status='RUN' and record.relationId in ?1")
	public List<MonitorRecordManyTimesPO> findNeedStop(Collection<Long> relationIds);

}
