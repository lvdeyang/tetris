package com.sumavision.tetris.zoom;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ZoomPO.class, idClass = Long.class)
public interface ZoomDAO extends BaseDAO<ZoomPO>{

	/**
	 * 根据号码查询会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午3:25:25
	 * @param String code 会议号码
	 * @return ZoomPO 会议
	 */
	public ZoomPO findByCode(String code);
	
}
