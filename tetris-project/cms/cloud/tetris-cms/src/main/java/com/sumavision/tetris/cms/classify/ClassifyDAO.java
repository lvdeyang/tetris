package com.sumavision.tetris.cms.classify;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ClassifyPO.class, idClass = Long.class)
public interface ClassifyDAO extends BaseDAO<ClassifyPO>{

}
