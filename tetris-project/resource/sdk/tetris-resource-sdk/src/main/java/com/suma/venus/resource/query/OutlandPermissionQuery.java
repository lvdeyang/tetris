package com.suma.venus.resource.query;

import java.util.List;
import java.util.Map;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

public class OutlandPermissionQuery {

	/**
	 * 查询文件夹的勾选情况和例外情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午5:04:23
	 * @param String serNodeNamePath 外域路径
	 * @param String folderPath 目录路径
	 * @param List<String> folders 子文件夹列表
	 * @param String devices 设备列表
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 * @return key:check;value:List<OutlandPermissionCheckPO> 勾选情况
	 * @return key:except;value:List<OutlandPermissionExceptPO> 例外情况
	 */
	public Map<String, Object> queryFolderPermissions(
			String serNodeNamePath,
			String folderPath,
			List<String> folders,
			String devices,
			String permissionType,
			Long roleId) throws Exception{
		
		return new HashMapWrapper<String, Object>().put("check", null)
												   .put("except", null)
												   .getMap();
	}
	
	/**
	 * 根据权限过滤设备，有权限的设备返回设备权限<br/>
	 * <p>
	 *   1.根据设备id判断
	 *       -例外表中存在设备授权：一定无权限
	 *       -勾选表中存在设备授权：一定有权限
	 *   2.根据父目录判断
	 *       勾选表目录路径length>例外表目录路径length：一定有权限
	 *       勾选表目录路径length<例外表目录路径length：一定无权限
	 *   3.除以上两种情况，勾选表目录不存在并且例外表不存在：一定无权限
	 *   4.勾选表与里外表都存在的情况不存在        
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:48:19
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String devices 设备列表
	 * @param Long roleId 角色id
	 */
	public void filterPermissions(
			String serNodeNamePath,
			String folderPath,
			String devices,
			Long roleId) throws Exception{
		
	}
	
}
