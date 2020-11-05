package com.sumavision.tetris.bvc.model.terminal.layout;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutPositionPO.class, idClass = Long.class)
public interface LayoutPositionDAO extends BaseDAO<LayoutPositionPO>{

	/**
	 * 查询布局内的排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午1:25:58
	 * @param Long layoutId 布局id
	 * @return List<LayoutPositionPO> 排版
	 */
	public List<LayoutPositionPO> findByLayoutId(Long layoutId);
	
}
