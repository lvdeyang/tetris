package com.sumavision.bvc.device.monitor.subtitle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorSubtitlePO.class, idClass = Long.class)
public interface MonitorSubtitleDAO extends MetBaseDAO<MonitorSubtitlePO>{

	/**
	 * 分页查询用户创建的字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 上午10:46:26
	 * @param String userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<MonitorSubtitlePO> 字幕列表
	 */
	public Page<MonitorSubtitlePO> findByUserId(String userId, Pageable page);
	
	/**
	 * 统计用户创建的字幕数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 上午11:06:53
	 * @param String userId 用户id
	 * @return int 统计结果
	 */
	public int countByUserId(String userId);
	
}
