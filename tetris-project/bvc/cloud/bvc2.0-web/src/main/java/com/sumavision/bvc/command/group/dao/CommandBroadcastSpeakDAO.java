package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastSpeakPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandBroadcastSpeakPO.class, idClass = long.class)
public interface CommandBroadcastSpeakDAO extends MetBaseDAO<CommandBroadcastSpeakPO>{

}
