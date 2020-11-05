package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = AuthorizationPO.class, idClass = long.class)
public interface AuthorizationDAO extends MetBaseDAO<AuthorizationPO>{

	public List<AuthorizationPO> findByName(String name);
	
}
