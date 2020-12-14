package com.sumavision.tetris.application.template;

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = TemplatePO.class, idClass = Long.class)
public interface TemplateDAO extends BaseDAO<TemplatePO>{

    public TemplatePO findByName(String name);

    public List<TemplatePO> findByNameNotNullAndBusinessTypeNotNullAndTaskTypeNotNullAndBodyNotNull();

}
