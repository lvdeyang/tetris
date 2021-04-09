package com.sumavision.bvc.device.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupBusinessRolePO.class, idClass = long.class)
public interface DeviceGroupBusinessRoleDAO extends MetBaseDAO<DeviceGroupBusinessRolePO>{

	/**
	 * @Title: 通过设备组类型找角色
	 * @param groupId 设备组id
	 * @return List<DeviceGroupBusinessRolePO> 角色PO List
	 * @throws
	 */
	public List<DeviceGroupBusinessRolePO> findByGroupId(Long groupId);

	/**
	 * @Title: 通过录制属性special和设备组id查找角色
	 * @param special 录制属性
	 * 		  groupId 设备组id
	 * @return List<DeviceGroupBusinessRolePO> 角色PO List
	 */
	public List<DeviceGroupBusinessRolePO> findByTypeAndGroupId(BusinessRoleType type, Long groupId);
	/**
	 * 获取设备组中特定类型的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月16日 下午5:09:03
	 * @param groupId 设备组id
	 * @param special 角色类型
	 * @return List<DeviceGroupBusinessRolePO> 角色
	 */
	public List<DeviceGroupBusinessRolePO> findByGroupIdAndSpecial(Long groupId, BusinessRoleSpecial special);
	/**
	 * 获取设备组中特定多個类型的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月16日 下午5:09:03
	 * @param groupId 设备组id
	 * @param specials 角色类型
	 * @return List<DeviceGroupBusinessRolePO> 角色
	 */
	public List<DeviceGroupBusinessRolePO> findByGroupIdAndSpecialIn(Long groupId, BusinessRoleSpecial specials);
		/**
	 * @Title: 获取设备组中非多个特定类型的角色<br/>
	 * @param groupId 设备组id
	 * @param specials 角色类型数组
	 * @return List<DeviceGroupBusinessRolePO> 角色
	 * @throws
	 */
	public List<DeviceGroupBusinessRolePO> findByGroupIdAndSpecialNotIn(Long groupId, Collection<BusinessRoleSpecial> specials);
}
