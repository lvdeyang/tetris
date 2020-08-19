package com.sumavision.tetris.zoom.webrtc;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = WebRtcRoomInfoPO.class, idClass = Long.class)
public interface WebRtcRoomInfoDAO extends BaseDAO<WebRtcRoomInfoPO>{

	/**
	 * 根据会议室id查询webrtc房间列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午5:17:21
	 * @param Long zoomId 会议室id
	 */
	public List<WebRtcRoomInfoPO> findByZoomId(Long zoomId);
	
}
