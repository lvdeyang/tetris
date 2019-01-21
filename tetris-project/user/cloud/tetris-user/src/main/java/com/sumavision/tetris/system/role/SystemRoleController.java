package com.sumavision.tetris.system.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/system/role")
public class SystemRoleController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	
	
}
