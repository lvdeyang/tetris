package com.sumavision.tetris.menu;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 菜单工具类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月20日 下午1:47:17
 */
@FeignClient(name = "tetris-menu", configuration = FeignConfiguration.class)
public interface MenuFeign {

	/**
	 * 获取有权限的菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月11日 下午3:44:52
	 * @param UserVO user 用户
	 * @return List<MenuVO> 菜单列表
	 */
	@RequestMapping(value = "/menu/feign/permission/menus")
	public JSONObject permissionMenus() throws Exception;
	
	/**
	 * 查询系统角色配置的首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午3:32:25
	 * @param Long roleId 系统角色id
	 * @return MenuVO 菜单
	 */
	@RequestMapping(value = "/menu/feign/query/home/page")
	public JSONObject queryHomePage(@RequestParam("roleId") Long roleId) throws Exception;
	
	/**
	 * 系统功能授权菜单查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午5:05:41
	 * @param Long roleId 系统角色id
	 * @return menus List<MenuVO> 菜单树
	 * @return authorized List<Long> 已授权的菜单id列表
	 * @return homePage Long 首页菜单id
	 */
	@RequestMapping(value = "/menu/feign/query/menus/by/role/id")
	public JSONObject queryMenusByRoleId(
			@RequestParam("roleId") Long roleId,
			@RequestParam("companyId") Long companyId,
			@RequestParam("handleCompanyQuery") Boolean handleCompanyQuery) throws Exception;
	
}