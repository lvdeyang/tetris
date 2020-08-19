package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;

@RepositoryDefinition(domainClass = WorkNodePO.class, idClass = Long.class)
public interface WorkNodeDao extends CommonDao<WorkNodePO>{

	public List<WorkNodePO> findByIp(String ip);
	
	public WorkNodePO findByNodeUid(String nodeUid);
	
	public List<WorkNodePO> findByNodeUidIn(Collection<String> nodeUids);
	
	public List<WorkNodePO> findByType(NodeType type);
	
	public WorkNodePO findTopByType(NodeType type);
	
	@Query("select node from WorkNodePO node where node.name like %?1%")
	public List<WorkNodePO> findByNameLike(String name);
}
