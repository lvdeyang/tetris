package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessGroupMemberPO.class, idClass = long.class)
public interface BusinessGroupMemberDAO extends MetBaseDAO<BusinessGroupMemberPO>{
	
	public List<BusinessGroupMemberPO> findByGroupId(Long groupId);
	
	@Query(value = "select * from BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where member.business_group_id = ?1 and m.role_id = ?2", nativeQuery = true)
	public List<BusinessGroupMemberPO> findByGroupIdAndRoleId(Long groupId, Long roleId);

	@Query(value = "select * from BVC_BUSINESS_GROUP_MEMBER member where member.business_group_id = ?1 and member.role_id = ?2", nativeQuery = true)
	public List<BusinessGroupMemberPO> findByNoJoinGroupIdAndRoleId(Long groupId, Long roleId);
	
	@Query(value = "select * from BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id = ?1", nativeQuery = true)
	public List<BusinessGroupMemberPO> findByRoleId(Long roleId);
	
	@Query(value = "select * from BVC_BUSINESS_GROUP_MEMBER member left join TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION m on member.id = m.group_member_id where m.role_id in ?1", nativeQuery = true)
	public List<BusinessGroupMemberPO> findByRoleIdIn(Collection<Long> roleIds);
	
//	/** 从groupId获取成员id列表 */
//	@Query(value = "select m.id from com.sumavision.tetris.bvc.business.group.GroupMemberPO m where m.groupId = ?1")
//	public List<Long> findIdsByGroupId(Long groupId);
	
	/** 从groupId获取成员OriginId列表 */
	@Query(value = "select m.originId from com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO m where m.group.id = ?1")
	public List<String> findOriginIdsByGroupId(Long groupId);
	
	/**
	 * 通过成员id集合查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月8日 下午2:28:10
	 * @param ids 成员的id集合
	 * @return
	 */
	public List<BusinessGroupMemberPO> findByIdIn(Collection<Long> ids);

	/** 从originId列表获取成员id列表，通常用来把userId列表转换为成员id列表 */
	@Query(value = "select m.id from com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO m where m.group.id = ?1 and m.originId in ?2")
	public List<Long> findIdsByGroupIdAndOriginIds(Long groupId, Collection<String> originIds);
	
	/**
	 * 根据businessId属性查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 上午9:40:35
	 * @param businessId
	 * @return
	 */
	public List<BusinessGroupMemberPO> findByBusinessId(String businessId);
	
	/**
	 * 根据号码查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月22日 上午11:51:52
	 * @param groupId 对应business_group_id，即外键约束的字段，不是business_id字段
	 * @param codes 号码list
	 * @return
	 */
	public List<BusinessGroupMemberPO> findByGroupIdAndCodeIn(Long groupId, List<String> codes);

//	public List<GroupMemberPO> findByGroupMemberTypeAndOriginId(GroupMemberType groupMemberType,String originId);
//	
//	public List<GroupMemberPO> findByGroupIdAndIdIn(Long groupId, Collection<Long> ids);
//	
//	public void deleteByGroupIdIn(Collection<Long> groupIds);
//	
//	public String findIdByGroupIdAndOriginId(Long groupId,Long originId);
}
