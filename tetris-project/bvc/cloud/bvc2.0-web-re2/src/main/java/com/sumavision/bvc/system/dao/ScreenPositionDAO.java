package com.sumavision.bvc.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.ScreenPositionPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = ScreenPositionPO.class, idClass = long.class)
public interface ScreenPositionDAO extends MetBaseDAO<ScreenPositionPO>{

}
