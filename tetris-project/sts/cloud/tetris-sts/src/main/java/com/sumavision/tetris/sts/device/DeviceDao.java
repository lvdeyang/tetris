package com.sumavision.tetris.sts.device;


import com.sumavision.tetris.sts.common.CommonConstants.DeviceType;
import com.sumavision.tetris.sts.common.CommonConstants.BackType;
import com.sumavision.tetris.sts.common.CommonDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author lxw
 */
@RepositoryDefinition(domainClass=DevicePO.class,idClass=Long.class)
public interface DeviceDao extends CommonDao<DevicePO> {

	public List<DevicePO> findByGroupId(Long groupId);
	
	public List<DevicePO> findByDeviceType(DeviceType deviceType);
	
	public List<DevicePO> findByGroupIdAndDeviceType(Long groupId, DeviceType deviceType);

	public List<DevicePO> findByGroupIdAndDeviceTypeAndBackType(Long groupId, DeviceType deviceType, BackType backType);

	public DevicePO findTopByName(String name);

	public List<DevicePO> findByNameContaining(String keyword);

	@Query("select id from DevicePO where deviceType='SDM2' or deviceType='SDM3'")
	public List<Long> findSdmDeviceId();

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.backType = ?2 where d.id = ?1")
	public Integer updateBackTypeById(Long id, BackType backType);
}
