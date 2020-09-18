package com.sumavision.bvc.device.monitor.record;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=MonitorRecordManyTimesPO.class, idClass=Long.class)
public interface MonitorRecordManyTimesDAO extends MetBaseDAO<MonitorRecordManyTimesPO> {
	
	

}
