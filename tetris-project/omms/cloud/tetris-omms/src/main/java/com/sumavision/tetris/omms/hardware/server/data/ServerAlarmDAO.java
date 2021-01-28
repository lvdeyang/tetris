package com.sumavision.tetris.omms.hardware.server.data;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServerAlarmPO.class, idClass = Long.class)
public interface ServerAlarmDAO extends BaseDAO<ServerAlarmPO>{

}
