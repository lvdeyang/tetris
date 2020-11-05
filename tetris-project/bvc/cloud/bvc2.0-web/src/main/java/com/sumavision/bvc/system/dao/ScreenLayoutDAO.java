package com.sumavision.bvc.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = ScreenLayoutPO.class, idClass = long.class)
public interface ScreenLayoutDAO extends MetBaseDAO<ScreenLayoutPO>{

}
