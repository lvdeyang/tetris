package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupPO.class, idClass = long.class)
public interface GroupDAO extends MetBaseDAO<GroupPO>{
	
	@Query(value="select group.id from TETRIS_BVC_BUSINESS_GROUP _group left join TETRIS_BVC_GROUP_DEMAND demand on demand.business_id = _group.id where demand.id in ?1", nativeQuery = true)
	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
	
/*
	public List<GroupPO> findByName(String name);

	public List<GroupPO> findByNameLike(String name);

	public List<GroupPO> findByNameAndUserId(String name, Long userId);
		
	public List<GroupPO> findByType(GroupType type);
	
	public List<GroupPO> findByStatus(GroupStatus status);
	
	@Query(value="SELECT g.status FROM bvc_command_group g WHERE g.id=?1", nativeQuery=true)
	public GroupStatus findStatusById(Long id);
	
//	@Query(value="SELECT g.id FROM bvc_command_group g", nativeQuery=true)
	@Query(value="select g.id from com.sumavision.bvc.command.group.basic.CommandGroupPO g")
	public List<Long> findAllIds();
	
	@Query(value="select d.group.id from com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO d where d.id in ?1")
	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
	
	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE member.user_id=?1", nativeQuery=true)
	public List<GroupPO> findByMemberUserId(Long userId);
	
	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE (member.user_id=?1 and (member.member_status='CONNECT' or member.is_administrator=true))", nativeQuery=true)
	public List<GroupPO> findEnteredGroupByMemberUserId(Long userId);
	
	@Query(value = "select id from com.sumavision.bvc.command.group.basic.CommandGroupPO where uuid=?1")
	public Long findIdByUuid(String uuid);
	*/
}
