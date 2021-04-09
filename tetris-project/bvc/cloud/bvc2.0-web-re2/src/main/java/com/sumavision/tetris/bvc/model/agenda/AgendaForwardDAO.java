package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.dao.BaseDAO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardPO.class, idClass = Long.class)
public interface AgendaForwardDAO extends MetBaseDAO<AgendaForwardPO>{

	/**
	 * 查询议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午9:12:59
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardPO> 转发列表
	 */
	public List<AgendaForwardPO> findByAgendaId(Long agendaId);
	
	public List<AgendaForwardPO> findByAgendaIdIn(Collection<Long> agendaIds);
	
	/**
	 * 根据businessId字段查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 上午9:31:08
	 * @param businessId
	 * @return
	 */
	public List<AgendaForwardPO> findByBusinessId(Long businessId);
	
	/**
	 * 根绝业务组id和议程转发类型查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午2:00:04
	 * @param id 业务组id
	 * @param agendaForwardBusinessType 转发类型
	 * @return
	 */
	public List<AgendaForwardPO> findByBusinessIdAndAgendaForwardBusinessType(Long id,BusinessInfoType agendaForwardBusinessType);
}
