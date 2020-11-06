package com.sumavision.bvc.control.device.group.contacts;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupContactsPO.class, idClass = Long.class)
public interface DeviceGroupContactsDAO extends MetBaseDAO<DeviceGroupContactsPO>{

	public void deleteByUserIdAndBundleId(Long userId, String bundleId);
	
	public List<String> findBundleIdByUserId(Long userId);
}
