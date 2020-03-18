package com.sumavision.signal.bvc.network.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.network.po.NetworkInputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = NetworkInputPO.class, idClass = Long.class)
public interface NetworkInputDAO extends BaseDAO<NetworkInputPO>{

}
