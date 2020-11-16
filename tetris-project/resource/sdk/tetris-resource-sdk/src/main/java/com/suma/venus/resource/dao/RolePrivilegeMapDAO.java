package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.RolePrivilegeMap;

@RepositoryDefinition(domainClass = RolePrivilegeMap.class, idClass = Long.class)
public interface RolePrivilegeMapDAO extends CommonDao<RolePrivilegeMap>{

	public RolePrivilegeMap findByRoleIdAndPrivilegeId(Long roleId, Long privilegeId);
	
	public List<RolePrivilegeMap> findByRoleIdAndPrivilegeIdIn(Long roleId, Collection<Long> privilegeIds);

	public List<RolePrivilegeMap> findByPrivilegeIdIn(Collection<Long> privilegeIds);
	
	@Query(value = "SELECT m.* from role_privilege_map m LEFT JOIN privilegepo p on m.privilege_id = p.id where p.resource_indentity in ?1", nativeQuery = true)
	public List<RolePrivilegeMap> findByResourceIdIn(Collection<String> resourceIds);
	
	@Query(value = "SELECT m.* from role_privilege_map m LEFT JOIN privilegepo p on m.privilege_id = p.id where m.role_id = ?1 and p.resource_indentity in ?2", nativeQuery = true)
	public List<RolePrivilegeMap> findByRoleIdAndResourceIdIn(Long roleId, Collection<String> resourceIds);
}
