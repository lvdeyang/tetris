package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.RolePrivilegeMap;

@RepositoryDefinition(domainClass = RolePrivilegeMap.class, idClass = Long.class)
public interface RolePrivilegeMapDAO extends CommonDao<RolePrivilegeMap>{

	public RolePrivilegeMap findByRoleIdAndPrivilegeId(Long roleId, Long privilegeId);
	
	public List<RolePrivilegeMap> findByRoleIdAndPrivilegeIdIn(Long roleId, Collection<Long> privilegeIds);
	
}
