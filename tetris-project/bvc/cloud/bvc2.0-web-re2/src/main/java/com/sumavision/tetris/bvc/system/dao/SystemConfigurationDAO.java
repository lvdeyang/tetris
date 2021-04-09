package com.sumavision.tetris.bvc.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.system.po.SystemConfigurationPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=SystemConfigurationPO.class,idClass=Long.class)
public interface SystemConfigurationDAO extends MetBaseDAO<SystemConfigurationPO>{
	
	public SystemConfigurationPO findByTotalSizeMbNotNull();

}
