package com.sumavision.tetris.bvc.model.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TerminalBundlePO.class, idClass = long.class)
public interface TerminalBundleDAO extends MetBaseDAO<TerminalBundlePO>{

}
