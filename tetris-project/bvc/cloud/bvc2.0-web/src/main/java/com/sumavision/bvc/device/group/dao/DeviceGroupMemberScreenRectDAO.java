package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenRectPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupMemberScreenRectPO.class, idClass = long.class)
public interface DeviceGroupMemberScreenRectDAO extends MetBaseDAO<DeviceGroupMemberScreenRectPO>{

}
