package com.suma.venus.resource.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/feign/resource")
public class ResourceFeignController {
	
	@Autowired
	private ResourceService resourceService;

	/**
	 * 方法概述<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 上午11:15:23
	 * @param userId
	 * @param terminalType
	 * @param request
	 * @return List<UserBO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/users/by/userId")
	public Object queryUsersByUserId(
			Long userId,
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		String _terminalType = (terminalType == null || "".equals(terminalType))?null: terminalType;
		
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.fromName(_terminalType));
		
		return users;
		
	}
	
}
