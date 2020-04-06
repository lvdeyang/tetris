package com.sumavision.signal.bvc.network.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.network.po.NetworkOutputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = NetworkOutputPO.class, idClass = Long.class)
public interface NetworkOutputDAO extends BaseDAO<NetworkOutputPO>{

	public List<NetworkOutputPO> findByBundleId(String bundleId);
	
}
