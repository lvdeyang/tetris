package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardDestinationPO.class, idClass = Long.class)
public interface AgendaForwardDestinationDAO extends BaseDAO<AgendaForwardDestinationPO>{
	
	/**
	 * 根据议程转发查询议程转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:20:34
	 * @param Long agendaForwardId 议程转发id
	 * @return List<AgendaForwardDestinationPO> 议程转发目的列表
	 */
	public List<AgendaForwardDestinationPO> findByAgendaForwardId(Long agendaForwardId);
	
	/**
	 * 根据议程转发查询议程转发目的（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:20:34
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @return List<AgendaForwardDestinationPO> 议程转发目的列表
	 */
	public List<AgendaForwardDestinationPO> findByAgendaForwardIdIn(Collection<Long> agendaForwardIds);
	
	/**
	 * 根据议程转发和目的查询转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午3:39:44
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @param Long destinationId 目的id
	 * @param DestinationType destinationType 目的类型
	 * @return List<AgendaForwardDestinationPO> 转发目的列表
	 */
	public List<AgendaForwardDestinationPO> findByAgendaForwardIdInAndDestinationIdAndDestinationType(Collection<Long> agendaForwardIds, Long destinationId, DestinationType destinationType);
	
	/**
	 * 根据议程转发和目的查询转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月4日 下午3:17:42
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @param Collection<Long> destinationIds 目的id列表
	 * @param DestinationType destinationType 目的类型
	 * @return List<AgendaForwardDestinationPO> 议程转发目的列表
	 */
	public List<AgendaForwardDestinationPO> findByAgendaForwardIdInAndDestinationIdInAndDestinationType(Collection<Long> agendaForwardIds, Collection<Long> destinationIds, DestinationType destinationType);

	/**
	 * 根据目的查询转发目的<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月29日 上午11:15:09
	 * @param destinationIds
	 * @param destinationType
	 * @return
	 */
	public List<AgendaForwardDestinationPO> findByDestinationIdInAndDestinationType(Collection<Long> destinationIds, DestinationType destinationType);
	
}
