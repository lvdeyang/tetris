package com.sumavision.tetris.business.common.dao;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.po.TemplatePO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Collection;
import java.util.List;

@RepositoryDefinition(domainClass = TemplateDAO.class, idClass = Long.class)
public interface TemplateDAO extends BaseDAO<TemplatePO>{


	
}
