package com.sumavision.tetris.application.structure;

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = StructurePO.class, idClass = Long.class)
public interface StructureDAO extends BaseDAO<StructurePO>{


}
