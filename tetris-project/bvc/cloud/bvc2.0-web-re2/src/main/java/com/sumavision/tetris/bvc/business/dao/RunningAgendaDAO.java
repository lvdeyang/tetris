package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RunningAgendaPO.class, idClass = long.class)
public interface RunningAgendaDAO extends MetBaseDAO<RunningAgendaPO>{
	
	public RunningAgendaPO findByGroupId(Long groupId);
	
	/**
	 * 适用于自定义议程按照议程id 查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月10日 下午1:54:33
	 * @param agendaId
	 * @return
	 */
	public RunningAgendaPO findByAgendaId(Long agendaId);
	
	public RunningAgendaPO findByGroupIdAndAgendaId(Long groupId, Long agendaId);
	
	@Deprecated
	public List<RunningAgendaPO> findByGroupIdAndAgendaIdIn(Long groupId, List<Long> agendaIds);
	
//	public List<RunningAgendaPO> findByGroupId(Long groupId);
	
	@Query(value = "select r.agendaId from com.sumavision.tetris.bvc.business.group.RunningAgendaPO r where r.groupId = ?1 and r.running = true")
	public List<Long> findRunningAgendaIdsByGroupId(Long groupId);
	
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
