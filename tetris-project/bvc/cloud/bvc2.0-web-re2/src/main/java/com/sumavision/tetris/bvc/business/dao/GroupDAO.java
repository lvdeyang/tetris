package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;
import com.sumavision.tetris.bvc.business.group.BusinessOrderGroupType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupPO.class, idClass = long.class)
public interface GroupDAO extends MetBaseDAO<GroupPO>{
	
	@Query(value = "select id from com.sumavision.tetris.bvc.business.group.GroupPO where uuid=?1")
	public Long findIdByUuid(String uuid);
	
	@Query(value="select group.id from TETRIS_BVC_BUSINESS_GROUP _group left join TETRIS_BVC_GROUP_DEMAND demand on demand.business_id = _group.id where demand.id in ?1", nativeQuery = true)
	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
	
	@Query(value="SELECT * FROM TETRIS_BVC_BUSINESS_GROUP _group INNER JOIN BVC_BUSINESS_GROUP_MEMBER member ON member.business_group_id=_group.id WHERE member.origin_id=?1", nativeQuery=true)
	public List<GroupPO> findByMemberOriginId(String originId);
	
	@Query(value="SELECT * FROM TETRIS_BVC_BUSINESS_GROUP _group INNER JOIN BVC_BUSINESS_GROUP_MEMBER member ON member.business_group_id=_group.id WHERE (member.origin_id=?1 and (member.group_member_status='CONNECT' or member.is_administrator=true))", nativeQuery=true)
	public List<GroupPO> findEnteredGroupByMemberOriginId(String originId);

	public List<GroupPO> findByName(String name);
	
	public List<GroupPO> findByUserId(Long userId);
	
	public List<GroupPO> findByNameLike(String name);
	
	/**
	 * 根据预约类型，预约开始时间，会议执行状态，是否要执行预约会议查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月6日 上午11:11:13
	 * @param groupType BusinessOrderGroupType 预约类型 
	 * @param beginTime 预约会议开始时间
	 * @param status 会议执行状态
	 * @param executeGroup 是否要执行预约
	 * @return
	 */
	public List<GroupPO> findByOrderGroupTypeAndOrderBeginTimeBeforeAndStatusAndExecuteGroup(BusinessOrderGroupType groupType, Date beginTime, GroupStatus status, Boolean executeGroup);
	
	/**
	 * 根据预约类型，预约结束时间，会议执行状态查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 上午9:30:31
	 * @param groupType BusinessOrderGroupType 预约类型 
	 * @param endTimes 预约会议结束时间
	 * @param status 会议执行状态
	 * @return
	 */
	public List<GroupPO> findByOrderGroupTypeAndOrderEndTimeBeforeAndStatus(BusinessOrderGroupType groupType, Date endTimes, GroupStatus status);
	
	/**
	 * 根据会议号码查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 上午9:32:55
	 * @param groupNumber 会议号码
	 * @return
	 */
	public GroupPO findByGroupNumber(Long groupNumber);
	
	public List<GroupPO> findByGroupNumberIn(Collection<Long> groupNumbers);
	
	@Query(value="select group.groupNumber from com.sumavision.tetris.bvc.business.group.GroupPO group where group.groupNumber in ?1")
	public List<Long> findGroupNumberByGroupNumberIn(Collection<Long> groupNumbers);
	
	/*
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
