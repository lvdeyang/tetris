package com.sumavision.tetris.cs.area;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = DivisionPO.class, idClass = Long.class)
public interface DivisionDAO extends BaseDAO<DivisionPO>{

}
