package com.sumavision.tetris.bvc.business.dispatch.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TetrisDispatchPO.class, idClass = long.class)
public interface TetrisDispatchDAO extends MetBaseDAO<TetrisDispatchPO>{
		
//	@Query(value="select d from com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchPO d where d.bundleId in ?1")
//	public List<TetrisDispatchPO> findByReletiveBundleIds(Collection<String> bundleIds);
	
	/** 根据用户id、目的bundleId、源bundleId查相关的调度 */
	@Query(value="SELECT DISTINCT dispatch.* FROM tetris_dispatch dispatch INNER JOIN tetris_dispatch_channel channel ON channel.dispatch_id=dispatch.id WHERE (dispatch.user_id in ?1 or dispatch.bundle_id in ?2 or channel.source_bundle_id in ?3)", nativeQuery=true)
	public List<TetrisDispatchPO> findByReletiveUserIdsAndBundleIds(Collection<String> userIds, Collection<String> dstBbundleIds, Collection<String> srcBbundleIds);
	
//	@Query(value="select d.group.id from com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO d where d.id in ?1")
//	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
//	
//	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE member.user_id=?1", nativeQuery=true)
//	public List<CommandGroupPO> findByMemberUserId(Long userId);
//	
//	@Query(value="SELECT * FROM bvc_command_group _group INNER JOIN bvc_command_group_member member ON member.group_id=_group.id WHERE (member.user_id=?1 and (member.member_status='CONNECT' or member.is_administrator=true))", nativeQuery=true)
//	public List<CommandGroupPO> findEnteredGroupByMemberUserId(Long userId);
	
}
