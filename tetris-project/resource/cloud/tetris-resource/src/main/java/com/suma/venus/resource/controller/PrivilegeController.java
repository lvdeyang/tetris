package com.suma.venus.resource.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.menu.MenuVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping("/privilege")
public class PrivilegeController extends ControllerBase{

	private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeController.class);
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MenuQuery menuQuery;
	
	/**
	 * 获取菜单<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:53:28
	 * @return data
	 */
	@RequestMapping("/queryMenu")
	@ResponseBody
	public Map<String, Object> queryMenu(HttpServletRequest request) {
		Map<String, Object> data = makeAjaxData();
		try {
			// 获取当前登陆用户名
			UserVO user = userQuery.current();
			
			if(user == null){
				throw new BaseException(StatusCode.TIMEOUT, "token失效，请重新登录！");
			}

			List<MenuVO> menus = menuQuery.permissionMenus(user);

			data.put("vueRouters", menus);
			data.put("status", 200);
			data.put("message", "操作成功");
		} catch (Exception e) {
			if(e instanceof BaseException){
				BaseException e1 = (BaseException) e;
				data.put(ERRMSG, e1.getMessage());
				data.put("message", e1.getMessage());
				data.put("status", e1.getCode().getCode());
			}else{
				LOGGER.error("Fail to query menu : ", e);
				data.put(ERRMSG, "内部错误");
				data.put("message", "内部错误");
				data.put("status", 401);
			}
			
		}
		return data;
	}
	
}
