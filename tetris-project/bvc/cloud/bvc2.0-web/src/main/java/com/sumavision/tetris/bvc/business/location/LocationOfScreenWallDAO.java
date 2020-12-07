package com.sumavision.tetris.bvc.business.location;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=LocationOfScreenWallPO.class,idClass=Long.class)
public interface LocationOfScreenWallDAO extends MetBaseDAO<LocationOfScreenWallPO>{
	
	public void deleteByLocationTemplateLayoutId(Long layoutId);
	
	public void deleteByLocationTemplateLayoutIdAndLocationXAfterOrLocationTemplateLayoutIdAndLocationYAfter(Long layoutId, Integer locationX, Long _layoutId, Integer locationY);
	
	public LocationOfScreenWallPO findByLocationTemplateLayoutIdAndDecoderBundleId(Long layoutId, String bundleId);
	
	public LocationOfScreenWallPO findByLocationTemplateLayoutIdAndLocationXAndLocationY(Long layoutId, Integer locationX, Integer locationY);
	
	public List<LocationOfScreenWallPO> findByLocationTemplateLayoutId(Long layoutId);
	
	public List<LocationOfScreenWallPO> findByIdIn(Collection<Long> ids);
	
	/**
	 * 根据转发任务id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午9:06:13
	 * @param id
	 * @param monitorLiveDeviceId
	 * @return
	 */
	public List<LocationOfScreenWallPO> findByMonitorLiveDeviceIdIn(List<Long> monitorLiveDeviceIds);
	
	/**
	 * 删除转发任务id对应屏幕墙<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月19日 下午3:44:22
	 * @param monitorLiveDeviceIdList
	 */
	public void deleteByMonitorLiveDeviceIdIn(Collection<Long> monitorLiveDeviceIdList);
	
	/**
	 * 根据屏幕墙布局非对应id和设备id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 下午2:19:55
	 * @param layoutId
	 * @param bundleId
	 * @return
	 */
	public LocationOfScreenWallPO findByLocationTemplateLayoutIdNotAndDecoderBundleId(Long layoutId, String bundleId);
	
	/**
	 * 通过时候由编码器和状态查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午9:06:13
	 * @param id
	 * @param monitorLiveDeviceId
	 * @return
	 */
	public List<LocationOfScreenWallPO> findByEncoderBundleIdNotNullAndStatus(LocationExecuteStatus status);

}
