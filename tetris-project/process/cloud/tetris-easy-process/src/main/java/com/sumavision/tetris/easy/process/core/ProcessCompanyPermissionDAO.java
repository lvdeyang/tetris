package com.sumavision.tetris.easy.process.core;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessCompanyPermissionPO.class, idClass = Long.class)
public interface ProcessCompanyPermissionDAO extends BaseDAO<ProcessCompanyPermissionPO>{

}
