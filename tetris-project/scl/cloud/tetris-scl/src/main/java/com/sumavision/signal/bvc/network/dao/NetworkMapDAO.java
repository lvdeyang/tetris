package com.sumavision.signal.bvc.network.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.network.po.NetworkMapPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = NetworkMapPO.class, idClass = Long.class)
public interface NetworkMapDAO extends BaseDAO<NetworkMapPO>{

}
