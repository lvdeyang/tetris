package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupDecoderSchemePO.class, idClass = long.class)
public interface CommandGroupDecoderSchemeDAO extends MetBaseDAO<CommandGroupDecoderSchemePO>{

}
