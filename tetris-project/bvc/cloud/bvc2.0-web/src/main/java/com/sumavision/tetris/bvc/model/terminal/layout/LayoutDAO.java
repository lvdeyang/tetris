package com.sumavision.tetris.bvc.model.terminal.layout;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutPO.class, idClass = Long.class)
public interface LayoutDAO extends BaseDAO<LayoutPO>{

	/**
	 * 根据名称查询布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午3:33:43
	 * @param String name 名称
	 * @return LayoutPO 布局
	 */
	public LayoutPO findByName(String name);
	
}
