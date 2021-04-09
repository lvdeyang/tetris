package com.sumavision.tetris.bvc.model.layout.forward;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CombineTemplatePO.class, idClass = Long.class)
public interface CombineTemplateDAO extends BaseDAO<CombineTemplatePO>{

}
