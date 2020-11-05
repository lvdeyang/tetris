package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupMemberRolePermissionPO.class, idClass = long.class)
public interface GroupMemberRolePermissionDAO extends MetBaseDAO<GroupMemberRolePermissionPO>{
	
	public GroupMemberRolePermissionPO findByGroupMemberId(Long groupMemberId);
	
	public List<GroupMemberRolePermissionPO> findByGroupMemberIdIn(List<Long> groupMemberIds);
	
	public List<GroupMemberRolePermissionPO> findByRoleIdIn(List<Long> roleIds);
	
	public GroupMemberRolePermissionPO findByRoleIdAndGroupMemberId(Long roleId, Long groupMemberId);
	
	public List<GroupMemberRolePermissionPO> findByRoleIdAndGroupMemberIdIn(Long roleId, List<Long> groupMemberIds);
	
	public List<GroupMemberRolePermissionPO> findByRoleIdInAndGroupMemberId(List<Long> roleIds, Long groupMemberId);
	
	public void deleteByRoleIdInAndGroupMemberId(List<Long> roleIds, Long groupMemberId);
}
