package com.sumavision.tetris.organization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/organization/user/permission")
public class OrganizationUserPermissionController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	@Autowired
	private OrganizationUserPermissionQuery organizationUserPermissionQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			@PathVariable Long organizationId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		
		
	}
	
}
