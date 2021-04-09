package com.sumavision.tetris.bvc.business.group.process;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupProceedRecordPO.class, idClass = long.class)
public interface GroupProceedRecordDAO extends MetBaseDAO<GroupProceedRecordPO>{
	
	/**
	 * 根据会议组id以及会议是否结束查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月6日 下午5:37:03
	 * @param groupId 业务组id
	 * @param finished true会议结束
	 */
	public GroupProceedRecordPO findByGroupIdAndFinished(Long groupId, Boolean finished);
	
	public Page<GroupProceedRecordPO> findByGroupIdOrderByStartTimeDesc(Long groupId, Pageable page);

	public List<GroupProceedRecordPO> findByGroupIdIn(Collection<Long> groupIds);
	
	public Page<GroupProceedRecordPO> findByUserIdOrderByStartTimeDesc(Long userId, Pageable page);
	
	public List<GroupProceedRecordPO> findByUserIdOrderByStartTimeDesc(Long userId);
	
}
