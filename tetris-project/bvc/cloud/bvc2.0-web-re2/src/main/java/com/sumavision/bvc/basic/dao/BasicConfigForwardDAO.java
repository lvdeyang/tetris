package com.sumavision.bvc.basic.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.basic.po.BasicConfigForwardPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BasicConfigForwardPO.class, idClass = Long.class)
public interface BasicConfigForwardDAO extends MetBaseDAO<BasicConfigForwardPO>{

}
