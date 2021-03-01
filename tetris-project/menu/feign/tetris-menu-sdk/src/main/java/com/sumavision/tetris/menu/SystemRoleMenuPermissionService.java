package com.sumavision.tetris.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemRoleMenuPermissionService {

	@Autowired
	private SystemRoleMenuPermissionFeign systemRoleMenuPermissionFeign;
	
	
	/**
	 * 系统角色绑定菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	public void add(Long roleId, Long menuId) throws Exception{
		systemRoleMenuPermissionFeign.add(roleId, menuId);
	}
	
	/**
	 * 系统角色解绑菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	public void remove(Long roleId, Long menuId) throws Exception{
		systemRoleMenuPermissionFeign.remove(roleId, menuId);
	}
	
	/**
	 * 设置首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	public void setHomePage(Long roleId, Long menuId) throws Exception{
		systemRoleMenuPermissionFeign.setHomePage(roleId, menuId);
	}
	
}
