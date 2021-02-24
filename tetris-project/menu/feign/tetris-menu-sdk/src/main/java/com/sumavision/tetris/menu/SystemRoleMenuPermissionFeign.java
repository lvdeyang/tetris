package com.sumavision.tetris.menu;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-menu", configuration = FeignConfiguration.class)
public interface SystemRoleMenuPermissionFeign {

	/**
	 * 系统角色绑定菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 * @return List<SystemRoleMenuPermissionVO> 添加的权限列表
	 */
	@RequestMapping(value = "/system/role/menu/permission/feign/add")
	public Object add(@RequestParam("roleId") Long roleId, @RequestParam("menuId") Long menuId) throws Exception;
	
	/**
	 * 系统角色解绑菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	@RequestMapping(value = "/system/role/menu/permission/feign/remove")
	public Object remove(@RequestParam("roleId") Long roleId, @RequestParam("menuId") Long menuId) throws Exception;
	
	/**
	 * 设置首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	@RequestMapping(value = "/system/role/menu/permission/feign/set/home/page")
	public Object setHomePage(@RequestParam("roleId") Long roleId, @RequestParam("menuId") Long menuId) throws Exception;
	
}
