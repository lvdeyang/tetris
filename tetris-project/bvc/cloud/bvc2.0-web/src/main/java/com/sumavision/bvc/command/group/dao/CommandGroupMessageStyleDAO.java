package com.sumavision.bvc.command.group.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.message.CommandGroupMessageStylePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupMessageStylePO.class, idClass = long.class)
public interface CommandGroupMessageStyleDAO extends MetBaseDAO<CommandGroupMessageStylePO>{
	
	public Page<CommandGroupMessageStylePO> findByUserId(Long userId, Pageable page);
	
}
