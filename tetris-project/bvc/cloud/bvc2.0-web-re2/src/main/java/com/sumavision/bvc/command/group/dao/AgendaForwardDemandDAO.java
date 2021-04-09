package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardDemandPO.class, idClass = Long.class)
public interface AgendaForwardDemandDAO extends MetBaseDAO<AgendaForwardDemandPO> {
	
	/**
	 * 根绝议程转发id分页查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午4:52:12
	 * @param agendaForwardIdList 议程转发id集合
	 * @param page 分页参数
	 * @return Page<AgendaForwardDemandPO>
	 */
	public Page<AgendaForwardDemandPO> findByAgendaForwardIdInOrBusinessId(List<Long> agendaForwardIdList, Long businessId, Pageable page);

	/**
	 * 根绝议程转发id查找<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年4月7日 下午2:18:44
	 * @param agendaForwardId
	 * @return
	 */
	public AgendaForwardDemandPO findByAgendaForwardId(Long agendaForwardId);
	
	/**
	 * 根绝议程转发id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午4:53:56
	 * @param agendaForwardIdList 议程转发id集合
	 */
	public List<AgendaForwardDemandPO> findByAgendaForwardIdIn(List<Long> agendaForwardIdList);
	
	/**
	 * 根据源号码和目的号码查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 下午1:40:23
	 * @param srcCode 源号码
	 * @param dstCode 目的号码
	 * @param BusinessId groupId
	 */
	public List<AgendaForwardDemandPO> findBySrcCodeAndDstCodeAndBusinessId(String srcCode, String dstCode, Long BusinessId);

}
