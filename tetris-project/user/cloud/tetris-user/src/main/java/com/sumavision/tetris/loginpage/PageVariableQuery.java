/**
 * 
 */
package com.sumavision.tetris.loginpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import antlr.collections.List;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午11:33:21
 */
@Component
public class PageVariableQuery {
	@Autowired
	private PageVariableDAO pageVariableDAO;
	
	public List getPageVariable(long loginPageId){
		return null;
	}
}
