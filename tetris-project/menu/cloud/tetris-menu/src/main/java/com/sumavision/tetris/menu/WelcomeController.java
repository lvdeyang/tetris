package com.sumavision.tetris.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "")
public class WelcomeController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MenuQuery menuQuery;
	
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception{
		ModelAndView mv = null;
		mv = new ModelAndView("web/menu/index");
		return mv;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/prepare/app")
	public Object prepareApp(HttpServletRequest request) throws Exception{
		
		Map<String, Object> appInfo = new HashMap<String, Object>();
		
		//用户信息
		UserVO user = userQuery.current();
		
		appInfo.put("user", user);
		
		//菜单信息
		List<MenuVO> menus = menuQuery.permissionMenus(user);
		
		appInfo.put("menus", menus);
		
		return appInfo;
	}
}
