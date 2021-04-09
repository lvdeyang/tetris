package com.sumavision.bvc.device.monitor.osd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorOsdPO.class, idClass = Long.class)
public interface MonitorOsdDAO extends MetBaseDAO<MonitorOsdPO>{

	/**
	 * 分页查询用户创建的屏幕图层配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午4:54:56
	 * @param String userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorOsdPO> 配置列表
	 */
	public Page<MonitorOsdPO> findByUserId(String userId, Pageable page);
	
	/**
	 * 统计用户创建的屏幕图层配置数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午4:55:58
	 * @param String userId 用户id
	 * @return int 统计结果
	 */
	public int countByUserId(String userId);
	
}
