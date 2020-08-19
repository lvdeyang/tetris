package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.message.CommandGroupMessagePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupMessagePO.class, idClass = long.class)
public interface CommandGroupMessageDAO extends MetBaseDAO<CommandGroupMessagePO>{

	public List<CommandGroupMessagePO> findByGroupId(Long groupId);
	
	public Page<CommandGroupMessagePO> findByGroupId(Long groupId, Pageable page);
	
}
