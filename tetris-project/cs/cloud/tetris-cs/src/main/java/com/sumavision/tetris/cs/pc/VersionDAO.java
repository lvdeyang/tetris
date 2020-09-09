package com.sumavision.tetris.cs.pc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = VersionPO.class, idClass = Long.class)
public interface VersionDAO extends BaseDAO<VersionPO>{
	
	
}
