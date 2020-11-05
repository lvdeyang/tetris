package com.sumavision.signal.bvc.entity.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CapacityPermissionPortPO.class, idClass = long.class)
public interface CapacityPermissionPortDAO extends BaseDAO<CapacityPermissionPortPO>{

	public CapacityPermissionPortPO findByBundleId(String bundleId);
	
	public List<CapacityPermissionPortPO> findByCapacityIp(String capacityIp);
	
	public CapacityPermissionPortPO findByBundleIdAndChannelId(String bundleId, String channelId);

	public Integer countAllByCapacityIpAndCapacityPort(String transformIp, Long transformPort);

	public Integer countAllByCapacityIpAndTaskIdNotNull(String transformIp);

}
