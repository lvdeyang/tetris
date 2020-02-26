package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupUserInfoPO.class, idClass = long.class)
public interface CommandGroupUserInfoDAO extends MetBaseDAO<CommandGroupUserInfoPO>{

	/** 注意判空！未登录的用户查不到info */
	public CommandGroupUserInfoPO findByUserId(Long userId);
	
	public CommandGroupUserInfoPO findByUserName(String userName);
	
}
