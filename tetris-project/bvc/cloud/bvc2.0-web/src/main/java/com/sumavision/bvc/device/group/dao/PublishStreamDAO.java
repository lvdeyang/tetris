package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = PublishStreamPO.class, idClass = long.class)
public interface PublishStreamDAO extends MetBaseDAO<PublishStreamPO>{

}
