package com.sumavision.bvc.command.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.system.po.CommandSystemRolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandSystemRolePO.class, idClass = long.class)
public interface CommandSystemRoleDAO extends MetBaseDAO<CommandSystemRolePO>{

}
