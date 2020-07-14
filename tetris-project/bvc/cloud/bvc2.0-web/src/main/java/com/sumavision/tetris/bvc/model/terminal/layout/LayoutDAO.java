package com.sumavision.tetris.bvc.model.terminal.layout;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutPO.class, idClass = Long.class)
public interface LayoutDAO extends BaseDAO<LayoutPO>{

}
