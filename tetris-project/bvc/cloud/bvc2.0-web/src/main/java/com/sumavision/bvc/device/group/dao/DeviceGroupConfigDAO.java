package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupConfigPO.class, idClass = long.class)
public interface DeviceGroupConfigDAO extends MetBaseDAO<DeviceGroupConfigPO>{
	
	/**
	 * @Title: 查询设备组中的议程 
	 * @param groupId 设备组id
	 * @param page 分页
	 * @return Page<DeviceGroupConfigDTO> 议程
	 */
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO(config.id, config.uuid, config.updateTime, config.name, config.remark, config.type, config.run, config.audioOperation) from DeviceGroupConfigPO config where config.group.id=?1 and type='AGENDA'")
	public Page<DeviceGroupConfigDTO> findAgendaByGroupIdOutPutDTO(Long groupId, Pageable page);
	
	/**
	 * @Title: 查询设备组中的方案
	 * @param groupId 设备组id
	 * @param page 分页
	 * @return Page<DeviceGroupConfigDTO> 方案
	 */
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO(config.id, config.uuid, config.updateTime, config.name, config.remark, config.type) from DeviceGroupConfigPO config where config.group.id=?1 and type='SCHEME'")
	public Page<DeviceGroupConfigDTO> findSchemeByGroupIdOutPutDTO(Long groupId, Pageable page);
	
	/**
	 * @Title: 查询config的音量值
	 * @param configId
	 * @return 音量值
	 */
	@Query("select config.volume from DeviceGroupConfigPO config where config.id=?1")
	public int findVolumeByConfigId(Long id);
	
	/**
	 * @Title: 根据configId查询groupId
	 * @param configId
	 * @return Long
	 */
	@Query("select config.group.id from DeviceGroupConfigPO config where config.id=?1")
	public Long findGroupIdByConfigId(Long configId);
	
	/**
	 * @Title: 通过名称和配置类型查询议程 
	 * @param： name 名称
	 * @param： type 配置类型
	 * @return List<DeviceGroupConfigPO> 议程/方案
	 */
	@Query("select config from DeviceGroupConfigPO config where config.name=?1 and config.type=?2 and config.group.id=?3")
	public DeviceGroupConfigPO findByNameAndTypeAndGroupId(String name, ConfigType type, Long groupId);
	
	/**
	 * @Title: findByGroupIdAndType 
	 * @param groupId 会议Id
	 * @param type 配置类型
	 * @return List<DeviceGroupConfigPO>
	 */
	public List<DeviceGroupConfigPO> findByGroupIdAndType(Long groupId, ConfigType type);
}
