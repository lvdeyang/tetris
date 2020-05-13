package com.sumavision.tetris.menu;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.user.UserVO;

/**
 * 菜单工具类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月20日 下午1:47:17
 */
@Component
public class MenuQuery {

	@Autowired
	private MenuFeign menuFeign;
	
	/**
	 * 获取有权限的菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月11日 下午3:44:52
	 * @param UserVO user 用户
	 * @return List<MenuVO> 菜单列表
	 */
	public List<MenuVO> permissionMenus(UserVO user) throws Exception{
		return JsonBodyResponseParser.parseArray(menuFeign.permissionMenus(user), MenuVO.class);
	}
	
	/**
	 * 查询系统角色配置的首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午3:32:25
	 * @param Long roleId 系统角色id
	 * @return MenuVO 菜单
	 */
	public MenuVO queryHomePage(Long roleId) throws Exception{
		return JsonBodyResponseParser.parseObject(menuFeign.queryHomePage(roleId), MenuVO.class);
	}
	
}
