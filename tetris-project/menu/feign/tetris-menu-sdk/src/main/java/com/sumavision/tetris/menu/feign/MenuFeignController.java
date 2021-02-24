package com.sumavision.tetris.menu.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/menu/feign")
public class MenuFeignController {

	@Autowired
	private MenuQuery menuQuery;
	
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
