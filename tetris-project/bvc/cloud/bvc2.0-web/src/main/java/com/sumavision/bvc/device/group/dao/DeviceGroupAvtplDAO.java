package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupAvtplPO.class, idClass = long.class)
public interface DeviceGroupAvtplDAO extends MetBaseDAO<DeviceGroupAvtplPO>{
	
	public DeviceGroupAvtplPO findByGroupId(Long GroupId);
}
