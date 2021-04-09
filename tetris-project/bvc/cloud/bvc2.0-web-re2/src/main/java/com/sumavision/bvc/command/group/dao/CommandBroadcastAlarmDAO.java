package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastAlarmPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandBroadcastAlarmPO.class, idClass = long.class)
public interface CommandBroadcastAlarmDAO extends MetBaseDAO<CommandBroadcastAlarmPO>{

}
