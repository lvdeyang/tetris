package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupMemberChannelPO.class, idClass = long.class)
public interface DeviceGroupMemberChannelDao extends MetBaseDAO<DeviceGroupMemberChannelPO> {
	


	
}
