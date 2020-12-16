package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.SerNodeRolePermissionPO;

@RepositoryDefinition(domainClass = SerNodeRolePermissionPO.class, idClass = Long.class)
public interface SerNodeRolePermissionDAO extends CommonDao<SerNodeRolePermissionPO>{
	
	public List<SerNodeRolePermissionPO> findBySerNodeIdIn(Collection<Long> serNodeIds);
	
	public List<SerNodeRolePermissionPO> findByRoleId(Long roleId);
	
	public List<SerNodeRolePermissionPO> findBySerNodeId(Long serNodeId);
	
	public List<SerNodeRolePermissionPO> findByRoleIdIn(Collection<Long> roleIds);

}
