package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.demand.GroupDemandPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupDemandPO.class, idClass = long.class)
public interface GroupDemandDAO extends MetBaseDAO<GroupDemandPO>{
	
	public List<GroupDemandPO> findByIdIn(List<Long> ids);
	
	public List<GroupDemandPO> findByBusinessId(String businessId);
	
	public List<GroupDemandPO> findByDstMemberIdIn(List<Long> dstMemberIds);
	
//	@Query(value="select demand.id from TETRIS_BVC_GROUP_DEMAND demand left join TETRIS_BVC_BUSINESS_GROUP _group on demand.business_id = _group.id where group.id = ?1", nativeQuery = true)
//	public List<Long> findAllIdsByDemandIds(List<Long> demandIds);
}
