package com.sumavision.bvc.device.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupProceedRecordPO.class, idClass = long.class)
public interface DeviceGroupProceedRecordDAO extends MetBaseDAO<DeviceGroupProceedRecordPO>{
	
	public DeviceGroupProceedRecordPO findByGroupIdAndFinished(Long groupId, Boolean finished);
	
	public Page<DeviceGroupProceedRecordPO> findByGroupIdOrderByStartTimeDesc(Long groupId, Pageable page);

	public List<DeviceGroupProceedRecordPO> findByGroupIdIn(Collection<Long> groupIds);
	
	public Page<DeviceGroupProceedRecordPO> findByUserIdOrderByStartTimeDesc(Long userId, Pageable page);
	
	public List<DeviceGroupProceedRecordPO> findByUserIdOrderByStartTimeDesc(Long userId);
}
