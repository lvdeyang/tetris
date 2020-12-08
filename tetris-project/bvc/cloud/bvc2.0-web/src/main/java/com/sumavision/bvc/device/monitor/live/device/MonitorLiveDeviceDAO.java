package com.sumavision.bvc.device.monitor.live.device;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
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
	 * 获取用户的点播设备任务<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月19日 上午10:33:32
	 * @param Long userId 用户id
	 * @return List<MonitorLiveDevicePO>
	 */
	public List<MonitorLiveDevicePO> findByUserId(Long userId);
	
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
	
	/**
	 * 根据解码器id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午4:51:42
	 * @param videoBundleId
	 * @param dstVideoBundleId
	 * @return
	 */
	public MonitorLiveDevicePO findByDstVideoBundleId(String dstVideoBundleId);
	
	/**
	 * 根据userId和转发状态查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午4:32:10
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<MonitorLiveDevicePO> findByUserIdAndStatus(Long userId, MonitorRecordStatus status);
	
	/**
	 * 根据执行状态查询 <br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 上午10:13:44
	 * @param status 状态RUN/STOP
	 * @return Long 正在执行的转发条数
	 */
	public Long countByStatus(MonitorRecordStatus status);
	
	/**
	 * 通过视频源id或者目标设备id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月19日 下午1:08:00
	 * @return
	 */
	public List<MonitorLiveDevicePO> findByVideoBundleIdInOrDstVideoBundleIdIn(Collection<String> videoBundleIdList, Collection<String> dstVideoBundleIdList);
	
	/**
	 * 通过视频源和点播设备任务类型查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午3:14:43
	 * @param videoBundleId
	 * @param type
	 * @return
	 */
	public List<MonitorLiveDevicePO> findByVideoBundleIdAndType(String videoBundleId, LiveType type);
	
	/**
	 * 通过状态查询转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月7日 上午9:45:45
	 * @param status RUN/STOP
	 * @return
	 */
	public List<MonitorLiveDevicePO> findByStatus(MonitorRecordStatus status);
	
}
