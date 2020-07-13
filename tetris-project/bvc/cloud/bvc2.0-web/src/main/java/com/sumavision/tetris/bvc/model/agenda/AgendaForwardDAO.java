package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardPO.class, idClass = Long.class)
public interface AgendaForwardDAO extends BaseDAO<AgendaForwardPO>{

	/**
	 * 查询议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午9:12:59
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardPO> 转发列表
	 */
	public List<AgendaForwardPO> findByAgendaId(Long agendaId);
	
}
