package com.sumavision.bvc.meeting.logic.dao;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import com.sumavision.bvc.meeting.logic.po.MediaPushPO;
import com.sumavision.bvc.meeting.logic.po.OutConnMediaMuxPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MediaPushPO.class, idClass = long.class)
public interface MediaPushDao extends MetBaseDAO<MediaPushPO>{
	
	public OutConnMediaMuxPO getByUuid(@Param("uuid") String uuid);
	
	/**
	 * 根据设备通道查询任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 上午8:47:26
	 * @param String bundleId 设备id
	 * @param String channelId 通道id
	 * @return MediaPushPO 逻辑层调阅任务
	 */
	public MediaPushPO findByBundleIdAndChannelId(String bundleId, String channelId);
}
