package com.sumavision.bvc.device.monitor.live.device;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorLiveDevicePO.class, idClass = Long.class)
public interface MonitorLiveDeviceDAO extends MetBaseDAO<MonitorLiveDevicePO>{

	/**
	 * 获取用户的点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:51:10
	 * @param Long userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorLiveDevicePO> 点播设备任务列表
	 */
	public Page<MonitorLiveDevicePO> findByUserId(Long userId, Pageable page);
	
	/**
	 * 统计用户的点播设备任务数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:58:06
	 * @param Long userId 用户id
	 * @return int 数据量
	 */
	public int countByUserId(Long userId);
	
	/**
	 * 根据视频目的查询点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 下午1:35:12
	 * @param Collection<String> dstBundleIds 目的视频设备id列表
	 * @return List<MonitorLiveDevicePO> 点播设备任务列表
	 */
	public List<MonitorLiveDevicePO> findByDstVideoBundleIdIn(Collection<String> dstBundleIds);
	
	/**
	 * 根据视频解码和音频解码查询点播设备业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午1:21:56
	 * @param String dstVideoBundleId 视频解码设备id
	 * @param String dstVideoChannelId 视频解码通道id
	 * @param String dstAudioBundleId 音频解码设备id
	 * @param String dstAudioChannelId 音频解码通道id
	 * @return List<MonitorLiveDevicePO> 点播设备任务列表
	 */
	public List<MonitorLiveDevicePO> findByDstVideoBundleIdAndDstVideoChannelIdAndDstAudioBundleIdAndDstAudioChannelId(
			String dstVideoBundleId, String dstVideoChannelId, String dstAudioBundleId, String dstAudioChannelId);
	
}
