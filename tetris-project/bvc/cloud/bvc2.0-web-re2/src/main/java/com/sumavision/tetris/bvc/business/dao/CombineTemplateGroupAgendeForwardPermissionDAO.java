package com.sumavision.tetris.bvc.business.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineTemplateGroupAgendeForwardPermissionPO.class, idClass = long.class)
public interface CombineTemplateGroupAgendeForwardPermissionDAO extends MetBaseDAO<CombineTemplateGroupAgendeForwardPermissionPO>{

	public CombineTemplateGroupAgendeForwardPermissionPO findByGroupIdAndCombineTemplateIdAndAgendaForwardId(
			Long groupId, Long combineTemplateId, Long agendaForwardIdId);

	public List<CombineTemplateGroupAgendeForwardPermissionPO> findByGroupId(Long groupId);
	 
	/**
	 * 根据<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:02:14
	 * @param groupId
	 * @param terminalId
	 * @return
	 */
	public List<CombineTemplateGroupAgendeForwardPermissionPO> findByGroupIdAndTerminalId(Long groupId, Long terminalId);

	public List<CombineTemplateGroupAgendeForwardPermissionPO> findByGroupIdAndAgendaId(Long groupId, Long agendaId);
	
	/**
	 * 根据会议组id、议程id、议程转发id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午6:34:59
	 * @param groupId
	 * @param agendaId
	 * @param agendaForwardId
	 * @return
	 */
	public CombineTemplateGroupAgendeForwardPermissionPO findByGroupIdAndAgendaIdAndAgendaForwardId(Long groupId, Long agendaId, Long agendaForwardId);
}
