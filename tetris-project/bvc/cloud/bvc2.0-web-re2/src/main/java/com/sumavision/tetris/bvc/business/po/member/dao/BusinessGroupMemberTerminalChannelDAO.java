package com.sumavision.tetris.bvc.business.po.member.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessGroupMemberTerminalChannelPO.class, idClass = Long.class)
public interface BusinessGroupMemberTerminalChannelDAO extends MetBaseDAO<BusinessGroupMemberTerminalChannelPO> {

	/**
	 * 根据会议成员通道查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月14日 下午3:41:55
	 * @param Collection<Long> ids 会议成员通道id列表
	 * @return List<Long> 会议成员id列表
	 */
	@Query(value = "SELECT ID, MEMBER_ID FROM BVC_BUSINESS_GROUP_MEMBER_CHANNEL WHERE ID IN ?1", nativeQuery = true)
	public List<Object[]> findGroupMemberIdByIdIn(Collection<Long> ids);
	
}
