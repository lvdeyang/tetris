package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;
import com.suma.venus.resource.pojo.PermissionType;
import com.suma.venus.resource.pojo.SourceType;

@RepositoryDefinition(domainClass = OutlandPermissionCheckPO.class, idClass = Long.class)
public interface OutlandPermissionCheckDao extends CommonDao<OutlandPermissionCheckPO>{

	/**
	 * 精确查询设备授权勾选<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 上午9:21:46
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param PermissionType permissionType 权限类型
	 * @param SourceType sourceType 授权源类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionCheckPO> 授权勾选列表
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndFolderPathAndDeviceIdAndPermissionTypeAndSourceTypeAndRoleId(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			PermissionType permissionType,
			SourceType sourceType,
			Long roleId);
	
	/**
	 * 精确查询文件夹授权勾选<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午2:12:19
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param PermissionType permissionType 权限类型
	 * @param SourceType sourceType 授权源类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionCheckPO> 文件夹授权勾选列表
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndFolderPathAndPermissionTypeAndSourceTypeAndRoleId(
			String serNodeNamePath,
			String folderPath,
			PermissionType permissionType,
			SourceType sourceType,
			Long roleId);
	
	/**
	 * 查询角色在某个域下的特定授权勾选<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 上午10:29:10
	 * @param String serNodeNamePath 域路径
	 * @param PermissionType permissionType 权限类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionCheckPO> 授权勾选列表
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndPermissionTypeAndRoleId(
			String serNodeNamePath,
			PermissionType permissionType,
			Long roleId);
	
	/**
	 * 批量查询文件夹授权勾选情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午1:49:28
	 * @param String serNodeNamePath 域路径
	 * @param List<String> folderPaths 目录路径列表
	 * @param PermissionType permissionType 权限类型
	 * @param SourceType sourceType 授权资源类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionCheckPO> 文件夹授权勾选情况
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndFolderPathInAndPermissionTypeAndSourceTypeAndRoleId(
			String serNodeNamePath,
			List<String> folderPaths,
			PermissionType permissionType,
			SourceType sourceType,
			Long roleId);
	
	/**
	 * 查询文件夹的授权勾选情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月10日 上午10:09:39
	 * @param String serNodeNamePath 域路径
	 * @param Collection<String> folderPaths 父文件夹列表
	 * @param SourceType sourceType 源类型
	 * @param Collection<Long> roleIds 角色id列表
	 * @return List<OutlandPermissionCheckPO> 文件夹勾选情况
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndFolderPathInAndSourceTypeAndRoleIdIn(
			String serNodeNamePath,
			Collection<String> folderPaths,
			SourceType sourceType,
			Collection<Long> roleIds);
	
	/**
	 * 批量查询设备的授权勾选情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月10日 上午10:15:37
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 文件夹路径
	 * @param Collection<String> deviceIds 设备id列表
	 * @param SourceType sourceType 源类型
	 * @param Collection<Long> roleIds 角色id列表
	 * @return List<OutlandPermissionCheckPO> 设备勾选情况
	 */
	public List<OutlandPermissionCheckPO> findBySerNodeNamePathAndFolderPathAndDeviceIdInAndSourceTypeAndRoleIdIn(
			String serNodeNamePath,
			String folderPath,
			Collection<String> deviceIds,
			SourceType sourceType,
			Collection<Long> roleIds);
	
	/**
	 * 查询角色的全部授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月12日 上午10:23:25
	 * @param PermissionType permissionType 权限类型
	 * @param Long roleId 角色id
	 * @return List<OutlandPermissionCheckPO> 授权勾选
	 */
	public List<OutlandPermissionCheckPO> findByPermissionTypeAndRoleId(PermissionType permissionType, Long roleId);
	
}
