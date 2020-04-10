package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.PrivilegePO.EPrivilegeType;

@RepositoryDefinition(domainClass = PrivilegePO.class, idClass = Long.class)
public interface PrivilegeDAO extends CommonDao<PrivilegePO>{

	public List<PrivilegePO> findBypCode(String pCode);
	
	public List<PrivilegePO> findByPrivilegeType(EPrivilegeType privilegeType);
	
	public List<PrivilegePO> findByResourceIndentityIn(Collection<String> resourceIndentities);
	
	public List<PrivilegePO> findByPrivilegeTypeAndServiceName(EPrivilegeType privilegeType,String serviceName);
	
//	@Query(value = "select privilege from RolePrivilegeMap as map join map.id.role as role join map.id.privilege as privilege where role.id=:roleId") 
//	public List<PrivilegePO> findByRoleId(@Param("roleId")Long roleId);
//	
//	@Query(value = "select privilege from RolePrivilegeMap as map join map.id.role as role join map.id.privilege as privilege where role.id=?1 and privilege.privilegeType=?2") 
//	public List<PrivilegePO> findByRoleIdAndType(Long roleId,EPrivilegeType type);
//	
//	@Query(value = "select privilege from RolePrivilegeMap as map join map.id.role as role join map.id.privilege as privilege where role.id=?1 and privilege.privilegeType != ?2") 
//	public List<PrivilegePO> findByRoleIdAndTypeNot(Long roleId,EPrivilegeType type);
	
	public PrivilegePO findByResourceIndentity(String resourceIndentity);
	
	@Query(value = "from PrivilegePO p where p.privilegeType != ?1")
	public List<PrivilegePO> findByPrivilegeTypeNot(EPrivilegeType privilegeType);
	
	@Query("select max(p.orderNum) from PrivilegePO p where p.pCode=?1")
	public Integer findMaxOrderNumByPcode(String pCode);
	
	@Query(value = "select * from privilegepo p left join role_privilege_map m on p.id = m.privilege_id where m.role_id = ?1", nativeQuery = true)
	public List<PrivilegePO> findByRoleId(Long roleId);
	
	@Query(value = "select * from privilegepo p left join role_privilege_map m on p.id = m.privilege_id where m.role_id in ?1", nativeQuery = true)
	public List<PrivilegePO> findByRoleIdIn(Collection<Long> roleIds);
	
	@Query(value = "select * from privilegepo where SUBSTRING_INDEX(resource_indentity,'-',1) not in ?1", nativeQuery = true)
	public List<PrivilegePO> findByNotIndentify(List<String> resources);
	
	@Query(value = "select * from privilegepo where SUBSTRING_INDEX(resource_indentity,'-',1) in ?1", nativeQuery = true)
	public List<PrivilegePO> findByIndentify(List<String> resources);
}
