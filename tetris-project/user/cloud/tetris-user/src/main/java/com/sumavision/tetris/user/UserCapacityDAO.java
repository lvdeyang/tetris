package com.sumavision.tetris.user;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = UserCapacityPO.class, idClass = Long.class)
public interface UserCapacityDAO extends BaseDAO<UserCapacityPO>{
	
}
