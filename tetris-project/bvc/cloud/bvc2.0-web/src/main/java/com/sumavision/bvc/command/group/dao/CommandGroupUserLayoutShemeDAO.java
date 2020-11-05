package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserLayoutShemePO.class, idClass = long.class)
public interface CommandGroupUserLayoutShemeDAO extends MetBaseDAO<CommandGroupUserLayoutShemePO>{

}
