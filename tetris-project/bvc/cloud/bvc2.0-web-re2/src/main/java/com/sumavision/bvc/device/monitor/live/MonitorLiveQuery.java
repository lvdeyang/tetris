package com.sumavision.bvc.device.monitor.live;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 直播查询业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:20:45
 */
@Component
public class MonitorLiveQuery {

	@Autowired
	private MonitorLiveDAO monitorLiveDao;
	
	/**
	 * 分页查询直播调度任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午4:18:20
	 * @param String userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorLivePO> 任务列表
	 */
	public List<MonitorLivePO> findByUserId(Long userId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorLivePO> pagedEntities = monitorLiveDao.findByUserId(userId.toString(), page);
		return pagedEntities.getContent();
	}
	
	/**
	 * 根据源和目的查询直播任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午10:10:18
	 * @param String videoBundleId 源视频设备id
	 * @param String videoChannelId 源视频通道id
	 * @param String audioBundleId 源音频设备id
	 * @param String audioChannelId 源音频通道id
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @return List<MonitorLivePO> 直播任务
	 */
	public MonitorLivePO findBySrcAndDst(
			String videoBundleId, 
			String videoChannelId, 
			String audioBundleId, 
			String audioChannelId,
			String dstVideoBundleId, 
			String dstVideoChannelId, 
			String dstAudioBundleId, 
			String dstAudioChannelId) throws Exception{
		List<MonitorLivePO> tasks = monitorLiveDao.findByVideoBundleIdAndVideoChannelIdAndAudioBundleIdAndAudioChannelIdAndDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(
								videoBundleId, videoChannelId, audioBundleId, audioChannelId, dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId);
	
		if(tasks==null || tasks.size()<=0) return null;
		return tasks.get(0);
	}
	
}
