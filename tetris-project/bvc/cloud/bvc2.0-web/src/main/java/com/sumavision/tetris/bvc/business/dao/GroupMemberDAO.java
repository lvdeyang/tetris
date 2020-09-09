package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupMemberPO.class, idClass = long.class)
public interface GroupMemberDAO extends MetBaseDAO<GroupMemberPO>{
	
	public List<GroupMemberPO> findByGroupId(Long groupId);
	
	@Query(value = "select * from TETRIS_BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where member.group_id = ?1 and m.role_id = ?2", nativeQuery = true)
	public List<GroupMemberPO> findByGroupIdAndRoleId(Long groupId, Long roleId);
	
	@Query(value = "select * from TETRIS_BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id = ?1", nativeQuery = true)
	public List<GroupMemberPO> findByRoleId(Long roleId);
	
	@Query(value = "select * from TETRIS_BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id in ?1", nativeQuery = true)
	public List<GroupMemberPO> findByRoleIdIn(Collection<Long> roleIds);
	
	/** 从groupId获取成员id列表 */
	@Query(value = "select m.id from com.sumavision.tetris.bvc.business.group.GroupMemberPO m where m.groupId = ?1")
	public List<Long> findIdsByGroupId(Long groupId);
	
	/** 从groupId获取成员OriginId列表 */
	@Query(value = "select m.originId from com.sumavision.tetris.bvc.business.group.GroupMemberPO m where m.groupId = ?1")
	public List<String> findOriginIdsByGroupId(Long groupId);
	
	/** 从originId列表获取成员id列表，通常用来把userId列表转换为成员id列表 */
	@Query(value = "select m.id from com.sumavision.tetris.bvc.business.group.GroupMemberPO m where m.groupId = ?1 and m.originId in ?2")
	public List<Long> findIdsByGroupIdAndOriginIds(Long groupId, Collection<String> originIds);
	
	public List<GroupMemberPO> findByGroupMemberTypeAndOriginId(GroupMemberType groupMemberType,String originId);
	
	public List<GroupMemberPO> findByGroupIdAndIdIn(Long groupId, Collection<Long> ids);
	
	public void deleteByGroupIdIn(Collection<Long> groupIds);
	
	public String findIdByGroupIdAndOriginId(Long groupId,Long originId);
}
