package com.sumavision.bvc.device.monitor.point;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorPointPO.class, idClass = Long.class)
public interface MonitorPointDAO extends MetBaseDAO<MonitorPointPO>{

	/**
	 * 获取设备的预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午10:56:12
	 * @param String bundleId 设备id
	 * @return List<MonitorPointPO> 预置点列表
	 */
	public List<MonitorPointPO> findByBundleId(String bundleId);
	
	/**
	 * 获取设备预置点数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:08:02
	 * @param String bundleId 设备id
	 * @return int 预置点数量
	 */
	public int countByBundleId(String bundleId);
	
	/**
	 * 根据设备id和预置点序号查询预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午5:19:06
	 * @param String bundleId 设备id
	 * @param String index 预置点序号
	 * @return List<MonitorPointPO> 预置点列表
	 */
	public List<MonitorPointPO> findByBundleIdAndIndex(String bundleId, String index);
	
}
