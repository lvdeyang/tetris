package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TerminalBundleUserPermissionPO.class, idClass = long.class)
public interface TerminalBundleUserPermissionDAO extends MetBaseDAO<TerminalBundleUserPermissionPO>{
	
	public List<TerminalBundleUserPermissionPO> findByUserIdAndTerminalId(String userId, Long terminalId);
	
	public List<TerminalBundleUserPermissionPO> findByBundleIdIn(List<String> bundleIds);
	
//	@Query(value = "select * from GroupMemberPO member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id = ?1", nativeQuery = true)
//	public List<GroupMemberPO> findByRoleId(Long roleId);
//	
//	@Query(value = "select * from GroupMemberPO member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id in ?1", nativeQuery = true)
//	public List<GroupMemberPO> findByRoleIdIn(Collection<Long> roleIds);
	
}
