package com.sumavision.tetris.bvc.model.terminal;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalPO.class, idClass = Long.class)
public interface TerminalDAO extends BaseDAO<TerminalPO>{

	public TerminalPO findByType(TerminalType type);
}
