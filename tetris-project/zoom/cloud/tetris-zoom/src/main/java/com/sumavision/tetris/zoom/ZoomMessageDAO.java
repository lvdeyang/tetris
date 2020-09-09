package com.sumavision.tetris.zoom;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ZoomMessagePO.class, idClass = Long.class)
public interface ZoomMessageDAO extends BaseDAO<ZoomMessagePO>{

	/**
	 * 查询会议中的消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午4:22:21
	 * @param Long zoomId 会议id
	 * @return List<ZoomMemberPO> 消息列表
	 */
	public List<ZoomMessagePO> findByZoomId(Long zoomId);
	
}
