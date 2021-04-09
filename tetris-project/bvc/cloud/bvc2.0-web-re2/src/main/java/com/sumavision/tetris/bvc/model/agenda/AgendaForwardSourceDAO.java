package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardSourcePO.class, idClass = Long.class)
public interface AgendaForwardSourceDAO extends BaseDAO<AgendaForwardSourcePO>{
	
	/**
	 * 根据议程查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:17:21
	 * @param Long agendaForwardId 议程转发id
	 * @return List<AgendaForwardSourcePO> 议程转发源列表
	 */
	public List<AgendaForwardSourcePO> findByAgendaForwardId(Long agendaForwardId);
	
	/**
	 * 根据议程转发查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:16:31
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @return List<AgendaForwardSourcePO> 议程转发源列表
	 */
	public List<AgendaForwardSourcePO> findByAgendaForwardIdIn(Collection<Long> agendaForwardIds);
	
	/**
	 * 根据议程转发和源查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午3:35:32
	 * @param Collection<Long> agendaForwardIds 议程转发id列表
	 * @param Collection<Long> sourceIds 源id列表
	 * @param SourceType sourceType 源类型 
	 * @return 转发源列表
	 */
	public List<AgendaForwardSourcePO> findByAgendaForwardIdInAndSourceIdInAndSourceType(Collection<Long> agendaForwardIds, Collection<Long> sourceIds, SourceType sourceType);
	
	/**
	 * 根据议程转发和序号查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 下午4:45:09
	 * @param Long agendaForwardId 议程转发id
	 * @param Integer serialNum 序号
	 * @return List<AgendaForwardSourcePO> 源列表
	 */
	public List<AgendaForwardSourcePO> findByAgendaForwardIdAndSerialNum(Long agendaForwardId, Integer serialNum);
	
	/**
	 * 根据成员id列表查询议程转发源<br/>
	 * <p>只查询根据“成员通道”配置的源，不包含根据“角色通道”配置的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月28日 下午5:39:55
	 * @param memberIds
	 * @return
	 */
	@Query(value="SELECT * FROM TETRIS_BVC_MODEL_AGENDA_FORWARD_SOURCE source INNER JOIN BVC_BUSINESS_GROUP_MEMBER_CHANNEL channel ON channel.id=source.source_id WHERE source.source_type='GROUP_MEMBER_CHANNEL' and channel.member_id in ?1", nativeQuery=true)
	public List<AgendaForwardSourcePO> findAgendaForwardSourcesByMemberIds(List<Long> memberIds);
	
	public List<AgendaForwardSourcePO> findBySourceIdAndSourceType(Long sourceId, SourceType sourceType);
	
}
