/**
 * 
 */
package com.sumavision.tetris.loginpage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午2:52:14
 */

@Controller
@RequestMapping(value = "/login/page")
public class LoginPageController {
	
	@Autowired
	private LoginPageService loginPageService;
	
	@Autowired
	private LoginPageQuery loginPageQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object listLoginPage(HttpServletRequest request) throws Exception{
		
		return loginPageQuery.listLoginPage();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addLoginPage(String name,String remark,boolean isCurrent,String tpl) throws Exception{
		
		return loginPageService.addLoginPage(name, remark, isCurrent, tpl);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object deleteLoginPage(long id){
		loginPageService.deleteLoginPage(id);
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/use")
	public Object useLoginPage(long id){
		loginPageService.useLoginPage(id);
		return null;
	}
	
}
