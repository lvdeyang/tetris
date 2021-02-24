package com.sumavision.tetris.menu.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.menu.SystemRoleMenuPermissionService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/system/role/menu/permission/feign")
public class SystemRoleMenuPermissionFeignController {

	@Autowired
	private SystemRoleMenuPermissionService systemRoleMenuPermissionService;
	
	/**
	 * 系统角色绑定菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long roleId,
			Long menuId,
			HttpServletRequest request) throws Exception{
		
		systemRoleMenuPermissionService.add(roleId, menuId);
		return null;
	}
	
	/**
	 * 系统角色解绑菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long roleId,
			Long menuId,
			HttpServletRequest request) throws Exception{
		
		systemRoleMenuPermissionService.remove(roleId, menuId);
		return null;
	}
	
	/**
	 * 设置首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午6:07:38
	 * @param Long roleId 角色id
	 * @param Long menuId 菜单id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/home/page")
	public Object setHomePage(
			Long roleId,
			Long menuId,
			HttpServletRequest request) throws Exception{
		
		systemRoleMenuPermissionService.setHomePage(roleId, menuId);
		return null;
	}
	
}
