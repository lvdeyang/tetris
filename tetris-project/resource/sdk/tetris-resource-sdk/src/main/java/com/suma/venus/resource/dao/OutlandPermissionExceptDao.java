package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.OutlandPermissionExceptPO;
import com.suma.venus.resource.pojo.PermissionType;
import com.suma.venus.resource.pojo.SourceType;

@RepositoryDefinition(domainClass = OutlandPermissionExceptPO.class, idClass = Long.class)
public interface OutlandPermissionExceptDao extends CommonDao<OutlandPermissionExceptPO>{

	/**
	 * 精确查询授权例外<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 上午9:21:46
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param PermissionType permissionType 权限类型
	 * @param SourceType sourceType 授权源类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionExceptPO> 授权例外列表
	 */
	public List<OutlandPermissionExceptPO> findBySerNodeNamePathAndFolderPathAndDeviceIdAndPermissionTypeAndSourceTypeAndRoleId(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			PermissionType permissionType,
			SourceType sourceType,
			Long roleId);
	
}
