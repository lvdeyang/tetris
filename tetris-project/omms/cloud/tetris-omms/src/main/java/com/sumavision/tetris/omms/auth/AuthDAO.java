package com.sumavision.tetris.omms.auth;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AuthPO.class, idClass = Long.class)
public interface AuthDAO extends BaseDAO<AuthPO>{

	
}
