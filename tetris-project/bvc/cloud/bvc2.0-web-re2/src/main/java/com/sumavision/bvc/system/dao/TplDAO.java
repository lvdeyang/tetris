package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TplPO.class, idClass = long.class)
public interface TplDAO extends MetBaseDAO<TplPO>{
	public TplPO findByUuid(String uuid);
	
	public List<TplPO> findByName(String name);
}
