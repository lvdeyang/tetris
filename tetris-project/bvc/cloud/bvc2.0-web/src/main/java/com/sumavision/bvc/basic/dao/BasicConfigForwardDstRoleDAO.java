package com.sumavision.bvc.basic.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.basic.po.BasicConfigForwardDstRolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BasicConfigForwardDstRolePO.class, idClass = Long.class)
public interface BasicConfigForwardDstRoleDAO extends MetBaseDAO<BasicConfigForwardDstRolePO>{

}
