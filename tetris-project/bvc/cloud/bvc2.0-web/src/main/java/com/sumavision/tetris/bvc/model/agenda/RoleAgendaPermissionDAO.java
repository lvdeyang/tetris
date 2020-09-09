package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoleAgendaPermissionPO.class, idClass = Long.class)
public interface RoleAgendaPermissionDAO extends BaseDAO<RoleAgendaPermissionPO>{

	/**
	 * 查询议程关联的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午8:51:36
	 * @param Long agendaId 议程id
	 * @return List<RoleAgendaPermissionPO> 角色列表
	 */
	public List<RoleAgendaPermissionPO> findByAgendaId(Long agendaId);
	
}
