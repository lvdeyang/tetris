package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.GroupPublishStreamPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupPublishStreamPO.class, idClass = long.class)
public interface GroupPublishStreamDAO extends MetBaseDAO<GroupPublishStreamPO>{

	/**
	 * 通过会议组查找直播<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月26日 下午3:42:45
	 * @param groupId 会议组
	 * @return
	 */
	public GroupPublishStreamPO findByGroupId(Long groupId);
}
