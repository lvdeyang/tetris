package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutVirtualSourceTemplatePO.class, idClass = Long.class)
public interface LayoutVirtualSourceTemplateDAO extends BaseDAO<LayoutVirtualSourceTemplatePO>{
	
	public List<LayoutVirtualSourceTemplatePO> findByAgendaLayoutTemplateId(Long agendaLayoutTemplateId);
	
}
