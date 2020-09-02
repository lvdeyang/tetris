package com.sumavision.tetris.omms.software.service.deployment;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServiceDeploymentPO.class, idClass = Long.class)
public interface ServiceDeploymentDAO extends BaseDAO<ServiceDeploymentPO>{

}
