package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastSpeakerPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandBroadcastSpeakerPO.class, idClass = long.class)
public interface CommandBroadcastSpeakerDAO extends MetBaseDAO<CommandBroadcastSpeakerPO>{

}
