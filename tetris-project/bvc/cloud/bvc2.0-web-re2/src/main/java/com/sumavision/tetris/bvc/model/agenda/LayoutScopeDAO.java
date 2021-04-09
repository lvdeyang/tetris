package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutScopePO.class, idClass = Long.class)
public interface LayoutScopeDAO extends BaseDAO<LayoutScopePO>{

	/**
	 * 根据议程转发查询虚拟源选择域<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:27:11
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @return List<LayoutScopePO> 虚拟源选择域列表
	 */
	public List<LayoutScopePO> findByAgendaForwardIdIn(Collection<Long> agendaForwardIds);
	
	/**
	 * 根据议程转发和源数量查询虚拟源选择域<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 下午4:04:10
	 * @param agendaForwardId
	 * @param sourceNumber
	 * @return
	 */
	public LayoutScopePO findByAgendaForwardIdAndSourceNumber(Long agendaForwardId, Integer sourceNumber);
	
}
