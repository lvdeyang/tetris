package com.sumavision.tetris.device;

import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryDefinition(domainClass = DevicePO.class, idClass = Long.class)
public interface DeviceDao extends BaseDAO<DevicePO> {

	DevicePO findTopByName(String name);

	DevicePO findByDeviceIp(String deviceIp);

	List<DevicePO> findByIdIn(List<Long> ids);

	List<DevicePO> findByDeviceGroupId(Long deviceGroupId);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.funUnitStatus = ?1 where d.id = ?2")
	public Integer updateFunUnitStatusById(FunUnitStatus status, Long id);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.funUnitStatus = ?1 where d.deviceIp = ?2")
	public Integer updateFunUnitStatusByIp(FunUnitStatus status, String ip);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.funUnitStatus = ?1 where d.deviceIp = ?2 and d.devicePort=?3")
	public Integer updateFunUnitStatusByIpAndPort(FunUnitStatus status, String ip,Integer port);

	List<DevicePO> findByFunUnitStatus(FunUnitStatus funUnitStatus);


	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.backType = ?2 where d.id = ?1")
	public Integer updateBackTypeById(Long id, BackType backType);


	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DevicePO d set d.netConfig = ?2 where d.id = ?1")
	public Integer updateNetConfigById(Long id,Boolean netConfig);

	public List<DevicePO> findByNetConfig(Boolean netConfig);

	public List<DevicePO> findByDeviceGroupIdAndNetConfig(Long groupId,Boolean netConfig);
	public List<DevicePO> findByDeviceGroupIdAndBackTypeAndFunUnitStatusAndNetConfig(Long groupId,BackType backType,FunUnitStatus funUnitStatus,Boolean netConfig);

}
