package com.sumavision.bvc.command.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.system.po.CommandSystemTitlePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=CommandSystemTitlePO.class,idClass=Long.class)
public interface CommandSystemTitleDAO extends MetBaseDAO<CommandSystemTitlePO>{

	public CommandSystemTitlePO findByCurrentTaskEquals(boolean currentTest);
	
}
