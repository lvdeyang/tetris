package com.sumavision.tetris.cs.pc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cs/pcversion")
public class VersionController {
	@Autowired
	UserQuery userQuery;
	@Autowired
	VersionService versionService;
	@Autowired
	VersionQuery versionQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object add(String version,String url,String name,long size, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return versionService.edit(version, url,name,size);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/getVersion")
	public Object getVersion(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return versionQuery.getVersion();
	}
	
	
}
