package com.sumavision.bvc.device.monitor.playback;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorRecordPlaybackTaskPO.class, idClass = Long.class)
public interface MonitorRecordPlaybackTaskDAO extends MetBaseDAO<MonitorRecordPlaybackTaskPO>{

	/**
	 * 根据视频目的查询调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 上午8:26:56
	 * @param String dstVideoBundleId 目标视频设备id 
	 * @param String dstVideoChannelId 目标视频通道id
	 * @return List<MonitorRecordPlaybackTaskPO> 任务列表
	 */
	public List<MonitorRecordPlaybackTaskPO> findByDstVideoBundleIdAndDstVideoChannelIdAndUserId(String dstVideoBundleId, String dstVideoChannelId, String userId);
	
	/**
	 * 根据文件和目的查询调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午11:18:13
	 * @param String fileUuid 文件uuid
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String userId 业务用户id
	 * @param MonitorRecordPlaybackTaskType type 任务类型
	 * @return List<MonitorRecordPlaybackTaskPO> 任务列表
	 */
	public List<MonitorRecordPlaybackTaskPO> findByFileUuidAndDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserIdAndType(
			String fileUuid, String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId, String userId, MonitorRecordPlaybackTaskType type);
	
	/**
	 * 查询设备上的调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月27日 下午2:05:06
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String userId 业务用户id
	 * @return List<MonitorRecordPlaybackTaskPO> 任务列表
	 */
	public List<MonitorRecordPlaybackTaskPO> findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserId(
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId, String userId);
	
	/**
	 * 查询设备上的调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月27日 下午2:05:06
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @return List<MonitorRecordPlaybackTaskPO> 任务列表
	 */
	public List<MonitorRecordPlaybackTaskPO> findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId);
	
	/**
	 * 分页查询调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午2:45:02
	 * @param String userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorRecordPlaybackTaskPO> 任务列表
	 */
	public Page<MonitorRecordPlaybackTaskPO> findByUserId(String userId, Pageable page);
	
	/**
	 * 统计调阅任务数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午2:49:46
	 * @param String userId 用户id
	 * @return int 统计结果
	 */
	public int countByUserId(String userId);
	
}
