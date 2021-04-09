package com.sumavision.tetris.bvc.model.layout;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutPO.class, idClass = Long.class)
public interface LayoutDAO extends BaseDAO<LayoutPO>{

	/**
	 * 查找虚拟源的布局数量<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月19日 下午4:52:25
	 * @param positionNum 
	 * @return
	 */
	public List<LayoutPO> findByPositionNum(Long positionNum);
}
