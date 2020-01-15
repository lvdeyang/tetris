package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserPlayerPO.class, idClass = long.class)
public interface CommandGroupUserPlayerDAO extends MetBaseDAO<CommandGroupUserPlayerPO>{

	public CommandGroupUserPlayerPO findByBundleId(String bundleId);
}
