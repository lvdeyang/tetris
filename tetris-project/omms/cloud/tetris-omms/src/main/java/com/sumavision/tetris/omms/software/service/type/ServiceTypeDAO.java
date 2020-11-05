package com.sumavision.tetris.omms.software.service.type;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServiceTypePO.class, idClass = Long.class)
public interface ServiceTypeDAO extends BaseDAO<ServiceTypePO>{
	public List<ServiceTypePO> findByIdIn(Set<Long> id);

}
