package com.sumavision.bvc.system.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = AvtplGearsPO.class, idClass = long.class)
public interface AVtplGearsDAO extends MetBaseDAO<AvtplGearsPO>{

}
