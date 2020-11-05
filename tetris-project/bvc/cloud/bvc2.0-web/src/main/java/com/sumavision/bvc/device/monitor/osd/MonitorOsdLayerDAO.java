package com.sumavision.bvc.device.monitor.osd;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorOsdLayerPO.class, idClass = Long.class)
public interface MonitorOsdLayerDAO extends MetBaseDAO<MonitorOsdLayerPO>{

	/**
	 * 根据资源查询图层引用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:30:54
	 * @param MonitorOsdLayerType type 资源类型 
	 * @param Long contentId 资源id
	 * @return List<MonitorOsdLayerPO> 图层列表
	 */
	public List<MonitorOsdLayerPO> findByTypeAndContentId(MonitorOsdLayerType type, Long contentId);
	
}
