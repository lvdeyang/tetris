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
	 * 根据主键以及转发任务id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午9:06:13
	 * @param id
	 * @param monitorLiveDeviceId
	 * @return
	 */
	public LocationOfScreenWallPO findByIdAndMonitorLiveDeviceId(Long id, Long monitorLiveDeviceId);

}
