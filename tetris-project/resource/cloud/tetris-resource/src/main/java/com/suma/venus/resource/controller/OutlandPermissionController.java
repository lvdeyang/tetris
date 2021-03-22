package com.suma.venus.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.service.OutlandPermissionService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 外域授权<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月8日 下午4:25:11
 */
@Controller
@RequestMapping(value = "/outland/permission")
public class OutlandPermissionController {

	@Autowired
	private OutlandPermissionService outlandPermissionService;
	
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
	 * @param String name 设备名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/device/permission")
	public Object addDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		outlandPermissionService.addDevicePermission(serNodeNamePath, folderPath, deviceId, name, permissionType, roleId);
		return null;
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
	 * @param String name 目录名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/folder/permission")
	public Object addFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		outlandPermissionService.addFolderPermission(serNodeNamePath, folderPath, name, permissionType, roleId);
		return null;
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
	 * @param String name 设备名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/device/permission")
	public Object removeDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		outlandPermissionService.removeDevicePermission(serNodeNamePath, folderPath, deviceId, name, permissionType, roleId);
		return null;
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
	 * @param String name 目录名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/folder/permission")
	public Object removeFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		outlandPermissionService.removeFolderPermission(serNodeNamePath, folderPath, name, permissionType, roleId);
		return null;
	}
	
}
