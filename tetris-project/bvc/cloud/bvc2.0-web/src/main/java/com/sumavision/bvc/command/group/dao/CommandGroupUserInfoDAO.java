package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserInfoPO.class, idClass = long.class)
public interface CommandGroupUserInfoDAO extends MetBaseDAO<CommandGroupUserInfoPO>{

	public CommandGroupUserInfoPO findByUserId(Long userId);
	
	public CommandGroupUserInfoPO findByUserName(String userName);
	
}
