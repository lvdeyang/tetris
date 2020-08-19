package com.sumavision.bvc.device.monitor.live;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorLiveSplitConfigPO.class, idClass = Long.class)
public interface MonitorLiveSplitConfigDAO extends MetBaseDAO<MonitorLiveSplitConfigPO>{

	/**
	 * 根据用户和屏幕序号查询配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 上午11:10:52
	 * @param Long userId 用户id
	 * @param Integer serial 屏幕序号
	 * @return List<MonitorLiveSplitConfigPO> 配置列表
	 */
	public List<MonitorLiveSplitConfigPO> findByUserIdAndSerial(Long userId, Integer serial);
	
}
