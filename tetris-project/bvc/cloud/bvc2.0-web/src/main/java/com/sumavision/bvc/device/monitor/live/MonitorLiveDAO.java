package com.sumavision.bvc.device.monitor.live;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorLivePO.class, idClass = Long.class)
public interface MonitorLiveDAO extends MetBaseDAO<MonitorLivePO>{
 
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
	public List<MonitorLivePO> findByVideoBundleIdAndVideoChannelIdAndAudioBundleIdAndAudioChannelIdAndDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(
			String videoBundleId, String videoChannelId, String audioBundleId, String audioChannelId,
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId);
	
	/**
	 * 根据目的设备通道查询直播任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月27日 上午9:42:48
	 * @param String dstVideoBundleId 目的视频设备id
	 * @param String dstVideoChannelId 目的视频通道id
	 * @param String dstAudioBundleId 目的音频设备id
	 * @param String dstAudioChannelId 目的音频通道id
	 * @param String userId 业务用户id
	 * @return List<MonitorLivePO> 直播任务
	 */
	public List<MonitorLivePO> findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelIdAndUserId(
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId, String userId);
	
	/**
	 * 分页查询直播调度任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午4:14:14
	 * @param String userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorLivePO> 直播调度任务
	 */
	public Page<MonitorLivePO> findByUserId(String userId, Pageable page);
	
	/**
	 * 统计直播调度任务数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午4:15:20
	 * @param String userId 用户id
	 * @return int 任务数量
	 */
	public int countByUserId(String userId);
	
	/**
	 * 根据目标设备查询直播调阅任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月9日 上午11:28:05
	 * @param Collection<String> bundleIds 设备id列表
	 * @return List<MonitorLivePO> 任务列表
	 */
	public List<MonitorLivePO> findByDstVideoBundleIdIn(Collection<String> bundleIds);
	
}
