package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RunningAgendaPO.class, idClass = long.class)
public interface RunningAgendaDAO extends MetBaseDAO<RunningAgendaPO>{
	
	public RunningAgendaPO findByGroupIdAndAgendaId(Long groupId, Long agendaId);
	
	public List<RunningAgendaPO> findByGroupIdAndAgendaIdIn(Long groupId, List<Long> agendaIds);
	/*
	public List<GroupMemberPO> findByGroupId(Long groupId);
	
	@Query(value = "select * from GroupMemberPO member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where member.group_id = ?1 m.role_id = ?2", nativeQuery = true)
	public List<GroupMemberPO> findByGroupIdAndRoleId(Long groupId, Long roleId);
	
	@Query(value = "select * from GroupMemberPO member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id = ?1", nativeQuery = true)
	public List<GroupMemberPO> findByRoleId(Long roleId);
	
	@Query(value = "select * from GroupMemberPO member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id in ?1", nativeQuery = true)
	public List<GroupMemberPO> findByRoleIdIn(Collection<Long> roleIds);
	*/
}
