package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupRecordFragmentPO.class, idClass = long.class)
public interface CommandGroupRecordFragmentDAO extends MetBaseDAO<CommandGroupRecordFragmentPO>{
	
}
