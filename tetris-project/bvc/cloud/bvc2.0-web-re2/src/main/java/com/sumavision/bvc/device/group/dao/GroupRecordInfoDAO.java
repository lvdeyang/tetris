package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupRecordInfoPO.class, idClass = Long.class)
public interface GroupRecordInfoDAO extends MetBaseDAO<GroupRecordInfoPO> {

	public GroupRecordInfoPO findByGroupIdAndRun(Long groupId, boolean run);

	/**
	 * 根据会议id、创建会议用户id、录制状态查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:05:51
	 * @param groupId
	 * @param userId
	 * @param b
	 * @return
	 */
	public GroupRecordInfoPO findByGroupIdAndRecordUserIdAndRun(Long groupId, Long userId, boolean run);

	/**
	 * 根据会创建会议用户id、录制状态查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:32:35
	 * @param id
	 * @param b
	 * @return
	 */
	public List<GroupRecordInfoPO> findByRecordUserIdAndRun(Long id, boolean run);
}
