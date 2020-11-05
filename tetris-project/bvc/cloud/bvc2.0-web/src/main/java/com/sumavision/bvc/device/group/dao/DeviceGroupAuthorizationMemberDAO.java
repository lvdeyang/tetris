package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupAuthorizationMemberPO.class, idClass = long.class)
public interface DeviceGroupAuthorizationMemberDAO extends MetBaseDAO<DeviceGroupAuthorizationMemberPO>{			
}
