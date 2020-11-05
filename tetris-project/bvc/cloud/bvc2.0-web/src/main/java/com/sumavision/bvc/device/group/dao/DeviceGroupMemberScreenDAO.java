package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupMemberScreenPO.class, idClass = long.class)
public interface DeviceGroupMemberScreenDAO extends MetBaseDAO<DeviceGroupMemberScreenPO>{

}
