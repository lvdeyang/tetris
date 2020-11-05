package com.sumavision.signal.bvc.network.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.network.po.NetworkMapPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = NetworkMapPO.class, idClass = Long.class)
public interface NetworkMapDAO extends BaseDAO<NetworkMapPO>{

	public List<NetworkMapPO> findByOutputIdIn(Collection<Long> ids);
	
	@Query(value = "select m from TETRIS_SCL_NETWORK_MAP m left join TETRIS_SCL_NETWORK_OUTPUT o on m.output_id = o.id where o.bundle_id = ?1", nativeQuery = true)
	public List<NetworkMapPO> findByBundleId(String bundleId);
	
}
