package com.sumavision.bvc.device.group.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupProceedRecordPO.class, idClass = long.class)
public interface DeviceGroupProceedRecordDAO extends MetBaseDAO<DeviceGroupProceedRecordPO>{
	
	public DeviceGroupProceedRecordPO findByGroupIdAndFinished(Long groupId, Boolean finished);
	
	public Page<DeviceGroupProceedRecordPO> findByGroupId(Long groupId, Pageable page);
	
}
