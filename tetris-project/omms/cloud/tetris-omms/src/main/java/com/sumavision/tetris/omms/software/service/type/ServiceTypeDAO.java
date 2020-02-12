package com.sumavision.tetris.omms.software.service.type;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServiceTypePO.class, idClass = Long.class)
public interface ServiceTypeDAO extends BaseDAO<ServiceTypePO>{

}
