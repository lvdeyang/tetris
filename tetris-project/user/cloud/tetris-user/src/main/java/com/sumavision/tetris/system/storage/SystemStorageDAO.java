package com.sumavision.tetris.system.storage;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SystemStoragePO.class, idClass = Long.class)
public interface SystemStorageDAO extends BaseDAO<SystemStoragePO>{

}
