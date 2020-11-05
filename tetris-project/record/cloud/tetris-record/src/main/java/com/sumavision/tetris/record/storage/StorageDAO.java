package com.sumavision.tetris.record.storage;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = StoragePO.class, idClass = Long.class)
public interface StorageDAO extends BaseDAO<StoragePO> {

}
