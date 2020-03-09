package com.sumavision.tetris.zoom;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SourceGroupPO.class, idClass = Long.class)
public interface SourceGroupDAO extends BaseDAO<SourceGroupPO>{

}
