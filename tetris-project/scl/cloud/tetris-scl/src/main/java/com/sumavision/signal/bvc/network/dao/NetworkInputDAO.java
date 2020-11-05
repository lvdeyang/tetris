package com.sumavision.signal.bvc.network.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.network.po.NetworkInputPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = NetworkInputPO.class, idClass = Long.class)
public interface NetworkInputDAO extends BaseDAO<NetworkInputPO>{

	public List<NetworkInputPO> findByBundleId(String bundleId);
	
	public List<NetworkInputPO> findByBundleIdIn(Collection<String> bundleIds);
	
}
