package com.sumavision.tetris.bvc.page;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = PageTaskPO.class, idClass = long.class)
public interface PageTaskDAO extends MetBaseDAO<PageTaskPO>{
	
	public List<PageTaskPO> findByBusinessId(String businessId);

	public List<PageTaskPO> findByBusinessIdIn(Collection<String> businessIds);
	
	public List<PageTaskPO> findByForwardUuidIn(Collection<String> uuids);
	
	public List<PageTaskPO> findByDstMemberIdIn(Collection<Long> dstMemberIds);
	
//	@Query(value = "select * from PageTaskPO task left join TETRIS_BVC_BUSINESS_GROUP_MEMBER member on task.member_id = member.id where member.group_id = ?1", nativeQuery = true)
//	public List<PageTaskPO> findByGroupId(Long roleId);
	
	public List<PageTaskPO> findByBusinessInfoTypeAndPageInfoId(BusinessInfoType businessInfoType,Long pageInfoId);
	
	public Long countByBusinessInfoTypeIn(List<BusinessInfoType> types);
	
	public List<PageTaskPO> findByDstIdIn(Collection<String> dstIds);
}
