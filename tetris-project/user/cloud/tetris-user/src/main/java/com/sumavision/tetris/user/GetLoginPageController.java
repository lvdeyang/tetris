/**
 * 
 */
package com.sumavision.tetris.user;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月1日 下午2:18:49
 */
@Controller
@RequestMapping(value="/getLoginPage")
public class GetLoginPageController{
	
	@Autowired
	private GetLoginPageService getLoginPageService;
	
	@RequestMapping(value="/get")
	public void getLoginPage(HttpServletResponse response)throws Exception{
		
		String HTMLString = getLoginPageService.getLoginPageString();
		
		//返回默认登陆页面
		if(HTMLString==null){
			response.sendRedirect("/web/app/login/login.html");
			return;
		}
		response.setContentType("text/html;charset=UTF-8"); 
		response.getWriter().write(HTMLString);	
	}
}
