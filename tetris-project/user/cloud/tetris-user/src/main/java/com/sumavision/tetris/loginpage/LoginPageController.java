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
	
	
	/**
	 * 获取登陆页面列表<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午1:58:08
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object listLoginPage(HttpServletRequest request) throws Exception{
		
		return loginPageQuery.listLoginPage();
	}
	
	/**
	 * 添加登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午1:58:50
	 * @param name
	 * @param remark
	 * @param isCurrent
	 * @param tpl
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addLoginPage(String name,String remark,boolean isCurrent,String tpl) throws Exception{
		
		return loginPageService.addLoginPage(name, remark, isCurrent, tpl);
	}
	
	/**
	 * 删除登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午1:59:06
	 * @param id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object deleteLoginPage(long id){
		loginPageService.deleteLoginPage(id);
		return null;
	}
	
	/**
	 * 使用登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午1:59:18
	 * @param id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/use")
	public Object useLoginPage(long id){
		loginPageService.useLoginPage(id);
		return null;
	}
	
}
