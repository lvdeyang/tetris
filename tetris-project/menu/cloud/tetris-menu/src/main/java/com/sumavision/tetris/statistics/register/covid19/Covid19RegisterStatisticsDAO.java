package com.sumavision.tetris.statistics.register.covid19;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = Covid19RegisterStatisticsPO.class, idClass = Long.class)
public interface Covid19RegisterStatisticsDAO extends BaseDAO<Covid19RegisterStatisticsPO>{
	
}
