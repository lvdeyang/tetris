package com.sumavision.tetris.menu.feign;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.menu.MenuVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 菜单feign接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月16日 上午10:46:14
 */
@Controller
@RequestMapping(value = "/menu/feign")
public class MenuFeignController {

	@Autowired
	private MenuQuery menuQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取有权限的菜单（feign接口）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月16日 上午10:43:43
	 * @return List<MenuVO> 菜单列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/permission/menus")
	public Object permissionMenus(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<MenuVO> menus = menuQuery.permissionMenus(user);
		
		return menus;
	}
	
	/**
	 * 查询系统角色配置的首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午3:32:25
	 * @param Long roleId 系统角色id
	 * @return MenuVO 菜单
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/home/page")
	public Object queryHomePage(
			Long roleId, 
			HttpServletRequest request) throws Exception{
		
		return menuQuery.queryHomePage(roleId);
	}
	
	/**
	 * 系统功能授权菜单查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午5:05:41
	 * @param Long roleId 系统角色id
	 * @param Long companyId 企业id
	 * @param Boolean handleCompanyQuery 是否查询企业菜单
	 * @return menus List<MenuVO> 菜单树
	 * @return authorized List<Long> 已授权的菜单id列表
	 * @return homePage Long 首页菜单id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/menus/by/role/id")
	public Object queryMenusByRoleId(
			Long roleId,
			Long companyId,
			Boolean handleCompanyQuery,
			HttpServletRequest request) throws Exception{
		
		return menuQuery.queryMenusByRoleId(roleId, companyId, handleCompanyQuery);
	}
	
}
