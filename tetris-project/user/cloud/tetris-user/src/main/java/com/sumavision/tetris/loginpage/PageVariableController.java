/**
 * 
 */
package com.sumavision.tetris.loginpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午11:32:29
 */

@Controller
@RequestMapping(value = "/login/page")
public class PageVariableController {
	@Autowired
	private PageVariableQuery pageVariableQuery;
	
	@Autowired
	private PageVariableService pageVariableService;
	
	
	/**
	 * 获取页面配置的变量<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月27日 下午3:01:22
	 * @param loginPageId
	 * @return
	 */
	public Object getPageVariable(long loginPageId){
		
		return null;
	}
	
}
