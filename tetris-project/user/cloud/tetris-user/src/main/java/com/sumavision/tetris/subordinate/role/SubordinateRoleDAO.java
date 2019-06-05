package com.sumavision.tetris.subordinate.role;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SubordinateRolePO.class, idClass = Long.class)
public interface SubordinateRoleDAO extends BaseDAO<SubordinateRolePO>{
	public List<SubordinateRolePO> findByCompanyId(Long companyId);
}
