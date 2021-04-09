package com.sumavision.bvc.device.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupRecordSchemePO.class, idClass = long.class)
public interface DeviceGroupRecordSchemeDAO extends MetBaseDAO<DeviceGroupRecordSchemePO>{
	
	/**
	 * @Title: 根据设备组id查询录制角色
	 * @param groupId 设备组id
	 * @return List<Long>
	 */
	@Query("select rs.roleId from DeviceGroupRecordSchemePO rs where rs.group.id=?1")
	public List<Long> findRoleIdsByGroupId(Long groupId);
	
	@Modifying
	@Transactional
	public void deleteByRoleId(Long roleId);
	
	@Modifying
	@Transactional
	public void deleteByRoleIdIn(Collection<Long> roleIds);
	
	/**
	 * @Title: 根据设备组id查询录制角色
	 * @param groupId 设备组id
	 * @return List<DeviceGroupRecordSchemePO>
	 */
	public List<DeviceGroupRecordSchemePO> findByGroupId(Long groupId);
	
}
