package com.sumavision.bvc.device.monitor.live.user;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorLiveUserPO.class, idClass = Long.class)
public interface MonitorLiveUserDAO extends MetBaseDAO<MonitorLiveUserPO>{

	/**
	 * 查询xt用户已经点播的播放器设备id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 上午9:54:39
	 * @param Long userId xt用户id
	 * @return List<String> 已经点播的播放器设备id
	 */
	@Query(value = "SELECT DST_VIDEO_BUNDLE_ID FROM BVC_MONITOR_LIVE_USER WHERE USER_ID=?1 AND TYPE='XT_LOCAL'", nativeQuery = true)
	public List<String> findUsedBundleIdsByXtUserId(Long userId);
	
	/**
	 * 根据视频目的查询点播用户任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 下午1:48:02
	 * @param Collection<String> dstBundleIds 目的视频设备id列表
	 * @return List<MonitorLiveUserPO> 点播用户任务列表
	 */
	public List<MonitorLiveUserPO> findByDstVideoBundleIdIn(Collection<String> dstBundleIds);
	
	/**
	 * 获取用户的点播用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:51:10
	 * @param Long userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorLiveUserPO> 点播设备任务列表
	 */
	public Page<MonitorLiveUserPO> findByUserId(Long userId, Pageable page);
	
	/**
	 * 统计用户的点播用户任务数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:58:06
	 * @param Long userId 用户id
	 * @return int 数据量
	 */
	public int countByUserId(Long userId);
	
	/**
	 * 根据视频解码和音频解码查询点播用户业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午2:06:57
	 * @param String dstVideoBundleId 解码器视频设备id
	 * @param String dstVideoChannelId 解码器视频通道id
	 * @param String dstAudioBundleId 解码器音频设备id
	 * @param String dstAudioChannelId 解码器音频通道id
	 * @return List<MonitorLiveUserPO> 点播用户业务列表
	 */
	public List<MonitorLiveUserPO> findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId);
	
}
