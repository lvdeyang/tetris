package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.RoleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping("/user")
public class UserQueryController extends ControllerBase{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserQueryController.class);
	
	@Autowired
	private UserQueryFeign userFeign;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@RequestMapping(value="/getAllUser",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllUser(){
		Map<String, Object> data = makeAjaxData();
		try {
			List<UserBO> users = new ArrayList<UserBO>();
//			Map<String, List<UserBO>> resultMap = userFeign.queryUsers();
			users = userQueryService.queryAllUserBaseInfo(null);
			data.put("users", users);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询用户错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/getAllRoles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllRoles(){
		Map<String, Object> data = makeAjaxData();
		try {
//			Map<String, List<RoleBO>> map = userFeign.queryNonVirtualRoles();
//			Map<String, List<RoleBO>> map = userFeign.queryRoles();
			List<RoleBO> roles = userQueryService.queryAllRoles();
			data.put("roles", roles);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询错误");
		}
		
		return data;
	}
	
	@RequestMapping(value="/getLoginUserName",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getLoginUserName(HttpServletRequest request){
		Map<String, Object> data = makeAjaxData();
		try {
			UserBO user = userQueryService.current();
			data.put("userName", user.getName());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询错误");
		}
		
		return data;
	}
}
