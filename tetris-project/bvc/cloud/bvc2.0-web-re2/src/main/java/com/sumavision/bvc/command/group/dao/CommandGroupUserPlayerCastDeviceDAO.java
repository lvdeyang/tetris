package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserPlayerCastDevicePO.class, idClass = long.class)
public interface CommandGroupUserPlayerCastDeviceDAO extends MetBaseDAO<CommandGroupUserPlayerCastDevicePO>{

}
