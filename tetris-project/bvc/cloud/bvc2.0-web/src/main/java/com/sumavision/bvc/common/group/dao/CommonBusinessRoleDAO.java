package com.sumavision.bvc.common.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommonBusinessRolePO.class, idClass = long.class)
public interface CommonBusinessRoleDAO extends MetBaseDAO<CommonBusinessRolePO>{

	/**
	 * @Title: 通过设备组类型找角色
	 * @param groupId 设备组id
	 * @return List<DeviceGroupBusinessRolePO> 角色PO List
	 * @throws
	 */
//	public List<CommonBusinessRolePO> findByGroupId(Long groupId);

	/**
	 * @Title: 通过录制属性special和设备组id查找角色
	 * @param special 录制属性
	 * 		  groupId 设备组id
	 * @return List<DeviceGroupBusinessRolePO> 角色PO List
	 */
//	public List<CommonBusinessRolePO> findByTypeAndGroupId(BusinessRoleType type, Long groupId);
	/**
	 * 获取设备组中特定类型的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月16日 下午5:09:03
	 * @param groupId 设备组id
	 * @param special 角色类型
	 * @return List<CommonBusinessRolePO> 角色
	 */
//	public List<CommonBusinessRolePO> findByGroupIdAndSpecial(Long groupId, BusinessRoleSpecial special);
	/**
	 * 获取设备组中特定多個类型的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月16日 下午5:09:03
	 * @param groupId 设备组id
	 * @param specials 角色类型
	 * @return List<CommonBusinessRolePO> 角色
	 */
//	public List<CommonBusinessRolePO> findByGroupIdAndSpecialIn(Long groupId, BusinessRoleSpecial specials);
		/**
	 * @Title: 获取设备组中非多个特定类型的角色<br/>
	 * @param groupId 设备组id
	 * @param specials 角色类型数组
	 * @return List<CommonBusinessRolePO> 角色
	 * @throws
	 */
//	public List<CommonBusinessRolePO> findByGroupIdAndSpecialNotIn(Long groupId, Collection<BusinessRoleSpecial> specials);
}
