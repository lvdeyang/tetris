package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupForwardPO.class, idClass = long.class)
public interface CommandGroupForwardDAO extends MetBaseDAO<CommandGroupForwardPO>{
	
	public CommandGroupForwardPO findByDstVideoBundleIdAndExecuteStatus(String dstVideoBundleId, ExecuteStatus executeStatus);
	
	public List<CommandGroupForwardPO> findByExecuteStatus(ExecuteStatus executeStatus);

}
