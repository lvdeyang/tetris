package com.sumavision.tetris.omms.hardware.server;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServerPO.class, idClass = Long.class)
public interface ServerDAO extends BaseDAO<ServerPO>{

	
}
