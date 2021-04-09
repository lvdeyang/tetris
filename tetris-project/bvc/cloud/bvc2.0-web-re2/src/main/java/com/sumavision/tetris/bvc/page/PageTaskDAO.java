package com.sumavision.tetris.bvc.page;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = PageTaskPO.class, idClass = long.class)
public interface PageTaskDAO extends MetBaseDAO<PageTaskPO>{
	
	/**
	 * 查找某个group下的全部task<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午1:28:36
	 * @param _businessId groupId后边加“-”
	 * @param businessId groupId
	 * @return
	 */
	public List<PageTaskPO> findByBusinessIdStartingWithOrBusinessId(String _businessId, String businessId);
	
	public List<PageTaskPO> findByBusinessIdStartingWithAndBusinessInfoTypeNotOrBusinessIdAndBusinessInfoTypeNot(String _businessId, BusinessInfoType infoType, String businessId, BusinessInfoType businessInfoType);
	
	public List<PageTaskPO> findByBusinessIdAndBusinessInfoType(String businessId, BusinessInfoType businessInfoType);

	public List<PageTaskPO> findByBusinessIdIn(Collection<String> businessIds);
	
	public List<PageTaskPO> findByBusinessIdInAndBusinessInfoType(Collection<String> businessIds, BusinessInfoType businessInfoType);
	
	public List<PageTaskPO> findByDstMemberIdIn(Collection<Long> dstMemberIds);
	
//	@Query(value = "select * from PageTaskPO task left join TETRIS_BVC_BUSINESS_GROUP_MEMBER member on task.member_id = member.id where member.group_id = ?1", nativeQuery = true)
//	public List<PageTaskPO> findByGroupId(Long roleId);
	
	public List<PageTaskPO> findByBusinessInfoTypeAndPageInfoId(BusinessInfoType businessInfoType,Long pageInfoId);
	
	public Long countByBusinessInfoTypeIn(List<BusinessInfoType> types);
	
	public List<PageTaskPO> findByAgendaForwardSourceIdInAndBusinessId(Collection<Long> agedaForwardSourceId, String businessId);

	public List<PageTaskPO> findByCombineVideoUuid(String combineVideoUuid);
}
