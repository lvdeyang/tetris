package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupForwardDemandPO.class, idClass = long.class)
public interface CommandGroupForwardDemandDAO extends MetBaseDAO<CommandGroupForwardDemandPO>{

	public Page<CommandGroupForwardDemandPO> findByGroupId(Long groupId, Pageable page);
	
	public CommandGroupForwardDemandPO findByDstVideoBundleIdAndExecuteStatus(String dstVideoBundleId, ForwardDemandStatus executeStatus);
	
	public List<CommandGroupForwardDemandPO> findByExecuteStatus(ForwardDemandStatus executeStatus);
}
