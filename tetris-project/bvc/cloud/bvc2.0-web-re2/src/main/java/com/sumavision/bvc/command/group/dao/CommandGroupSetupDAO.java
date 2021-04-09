package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.setup.CommandGroupSetupPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupSetupPO.class, idClass = long.class)
public interface CommandGroupSetupDAO extends MetBaseDAO<CommandGroupSetupPO>{

}
