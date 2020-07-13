package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaLayoutTemplatePO.class, idClass = Long.class)
public interface AgendaLayoutTemplateDAO extends BaseDAO<AgendaLayoutTemplatePO>{

	/**
	 * 根据议程和角色查询布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午2:16:27
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @return List<AgendaLayoutTemplatePO> 布局信息列表
	 */
	public List<AgendaLayoutTemplatePO> findByAgendaIdAndRoleId(Long agendaId, Long roleId);
	
}
