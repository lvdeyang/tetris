package com.sumavision.tetris.mims.app.store;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = PreRemoveFilePO.class, idClass = Long.class)
public interface PreRemoveFileDAO extends BaseDAO<PreRemoveFilePO>{
	
}
