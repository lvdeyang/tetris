package com.sumavision.tetris.bvc.model.terminal.screen;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalScreenPO.class, idClass = Long.class)
public interface TerminalScreenDAO extends BaseDAO<TerminalScreenPO>{

}
