package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupAvtplGearsPO.class, idClass = long.class)
public interface DeviceGroupAvtplGearsDAO extends MetBaseDAO<DeviceGroupAvtplGearsPO>{

	public List<DeviceGroupAvtplGearsPO> findByAvtplId(Long avtplId);
}
