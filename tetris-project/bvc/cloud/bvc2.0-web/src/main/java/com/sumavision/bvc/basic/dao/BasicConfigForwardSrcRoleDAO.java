package com.sumavision.bvc.basic.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.basic.po.BasicConfigForwardSrcRolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BasicConfigForwardSrcRolePO.class, idClass = Long.class)
public interface BasicConfigForwardSrcRoleDAO extends MetBaseDAO<BasicConfigForwardSrcRolePO>{

}
