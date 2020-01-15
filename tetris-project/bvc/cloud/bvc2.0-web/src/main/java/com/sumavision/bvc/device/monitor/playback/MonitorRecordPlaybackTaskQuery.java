package com.sumavision.bvc.device.monitor.playback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 录制回放任务查询<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午8:28:17
 */
@Component
public class MonitorRecordPlaybackTaskQuery {

	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	/**
	 * 分页查询调阅任务列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午2:48:16
	 * @param String userId 用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorRecordPlaybackTaskPO> 调阅任务列表
	 */
	public List<MonitorRecordPlaybackTaskPO> findByUserId(String userId, int currentPage, int pageSize){
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		Page<MonitorRecordPlaybackTaskPO> pageedEntities = monitorRecordPlaybackTaskDao.findByUserId(userId, page);
		
		return pageedEntities.getContent();
	}
	
	/**
	 * 根据文件和目标查询调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午11:33:40
	 * @param String fileUuid 文件uuid
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String userId 业务用户id
	 * @param MonitorRecordPlaybackTaskType type 任务类型
	 * @return MonitorRecordPlaybackTaskPO 调阅任务
	 */
	public MonitorRecordPlaybackTaskPO findByFileAndDst(
			String fileUuid, 
			String dstVideoBundleId, 
			String dstVideoChannelId, 
			String dstAudioBundleId, 
			String dstAudioChannelId, 
			String userId,
			MonitorRecordPlaybackTaskType type){
		
		List<MonitorRecordPlaybackTaskPO> tasks = monitorRecordPlaybackTaskDao.findByFileUuidAndDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserIdAndType(
										fileUuid, dstVideoBundleId, dstVideoChannelId, dstAudioBundleId, dstAudioChannelId, userId, type);
		
		if(tasks==null || tasks.size()<=0) return null;
		return tasks.get(0);
	}
	
}
