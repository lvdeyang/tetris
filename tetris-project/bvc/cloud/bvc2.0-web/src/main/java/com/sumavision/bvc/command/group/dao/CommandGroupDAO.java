package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupPO.class, idClass = long.class)
public interface CommandGroupDAO extends MetBaseDAO<CommandGroupPO>{

	public CommandGroupPO findByName(String name);
	
	public List<CommandGroupPO> findByType(GroupType type);
	
	public List<CommandGroupPO> findByStatus(GroupStatus status);
	
	@Query(value="SELECT g.status FROM bvc_command_group g WHERE g.id=?1", nativeQuery=true)
	public GroupStatus findStatusById(Long id);
	
//	@Query(value="SELECT g.id FROM bvc_command_group g", nativeQuery=true)
	@Query(value="select g.id from com.sumavision.bvc.command.group.basic.CommandGroupPO g")
	public List<Long> findAllIds();
	
	@Query(value="select d.group.id from com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO d where d.id in ?1")
	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
	
	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE member.user_id=?1", nativeQuery=true)
	public List<CommandGroupPO> findByMemberUserId(Long userId);
	
	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE (member.user_id=?1 and (member.member_status='CONNECT' or member.is_administrator=true))", nativeQuery=true)
	public List<CommandGroupPO> findEnteredGroupByMemberUserId(Long userId);
	
}
