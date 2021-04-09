package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.setting.CommandGroupUserSettingPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserSettingPO.class, idClass = long.class)
public interface CommandGroupUserSettingDAO extends MetBaseDAO<CommandGroupUserSettingPO>{

}
