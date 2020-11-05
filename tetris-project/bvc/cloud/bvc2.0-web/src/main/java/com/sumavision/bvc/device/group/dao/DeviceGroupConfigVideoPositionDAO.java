package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupConfigVideoPositionPO.class, idClass = long.class)
public interface DeviceGroupConfigVideoPositionDAO extends MetBaseDAO<DeviceGroupConfigVideoPositionPO>{
	
}
