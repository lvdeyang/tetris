package com.sumavision.bvc.device.jv230.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.jv230.po.CombineJv230ConfigPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineJv230ConfigPO.class, idClass = long.class)
public interface CombineJv230ConfigDAO extends MetBaseDAO<CombineJv230ConfigPO>{

}
