package com.suma.venus.resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.OutlandPermissionCheckDao;
import com.suma.venus.resource.dao.OutlandPermissionExceptDao;
import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;
import com.suma.venus.resource.pojo.PermissionType;
import com.suma.venus.resource.pojo.SourceType;

@Service
public class OutlandPermissionService {

	@Autowired
	private OutlandPermissionCheckDao outlandPermissionCheckDao;
	
	@Autowired
	private OutlandPermissionExceptDao outlandPermissionExceptDao;
	
	/**
	 * 添加设备授权<br/>
	 * <p>
	 *   1.勾选表加一条设备授权<br/>
	 *   2.例外表中对应删除设备授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:25:33
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	public void addDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String permissionType,
			Long roleId) throws Exception{
		
		OutlandPermissionCheckPO permissionCheck = new OutlandPermissionCheckPO();
		permissionCheck.setSerNodeNamePath(serNodeNamePath);
		permissionCheck.setFolderPath(folderPath);
		permissionCheck.setDeviceId(deviceId);
		permissionCheck.setPermissionType(PermissionType.valueOf(permissionType));
		permissionCheck.setSourceType(SourceType.DEVICE);
		permissionCheck.setRoleId(roleId);
		outlandPermissionCheckDao.save(permissionCheck);
		
		
		
	}
	
	/**
	 * 添加文件夹授权<br/>
	 * <p>
	 *   1.勾选表加一条目录权限<br/>
	 *   2.勾选表中当前目录下的设备以及子目录和子目录设备的权限全部删除<br/>
	 *   3.例外表中本目录以及子目录授权全部删除<br/>
	 *   4.例外表中本目录以及子目录设备授权全部删除
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:27:54
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	public void addFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String permissionType,
			Long roleId) throws Exception{
		
	}
	
	/**
	 * 删除设备授权<br/>
	 * <p>
	 *   1.勾选表删除设备授权<br/>
	 *   2.比较勾选表中目录路径length>列外表中目录路径length 时在例外表中加一条设备授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:31:27
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	public void removeDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String permissionType,
			Long roleId) throws Exception{
		
	}
	
	/**
	 * 删除文件夹授权<br/>
	 * <p>
	 *   1.勾选表中本目录授权删除<br/>
	 *   2.勾选表中本目录下设备授权删除<br/>
	 *   3.勾选表子目录及子目录设备授权删除<br/>
	 *   4.例外表中子目录及子目录设备授权删除<br/>
	 *   5.勾选表父目录路径length>例外表父目录路径length 时在例外表中加一条目录授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:34:34
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	public void removeFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String permissionType,
			Long roleId) throws Exception{
		
	}
	
}
