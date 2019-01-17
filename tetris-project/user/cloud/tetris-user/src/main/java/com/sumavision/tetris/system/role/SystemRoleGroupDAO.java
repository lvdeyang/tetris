package com.sumavision.tetris.system.role;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SystemRoleGroupPO.class, idClass = Long.class)
public interface SystemRoleGroupDAO extends BaseDAO<SystemRoleGroupPO>{

}
