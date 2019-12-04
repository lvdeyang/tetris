package com.sumavision.tetris.auth.token;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TokenPO.class, idClass = Long.class)
public interface TokenDAO extends BaseDAO<TokenPO>{

}
