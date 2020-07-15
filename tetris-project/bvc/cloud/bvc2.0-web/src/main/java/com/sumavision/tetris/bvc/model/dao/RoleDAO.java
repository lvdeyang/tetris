package com.sumavision.tetris.bvc.model.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RolePO.class, idClass = long.class)
public interface RoleDAO extends MetBaseDAO<RolePO>{

	public RolePO findByInternalRoleType(InternalRoleType internalRoleType);
}
