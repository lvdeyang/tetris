package com.sumavision.tetris.omms.hardware.server.data.process;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;
@RepositoryDefinition(domainClass = ServerProcessUsagePO.class, idClass = Long.class)
public interface ServerProcessUsageDAO extends BaseDAO<ServerProcessUsagePO>{

}
