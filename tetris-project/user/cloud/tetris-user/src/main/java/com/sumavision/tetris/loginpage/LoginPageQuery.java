/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午3:28:05
 */
@Component
public class LoginPageQuery {
	private static final Logger LOG = LoggerFactory.getLogger(LoginPageQuery.class);
	@Autowired
	private LoginPageDAO loginPageDAO;
	
	public Object listLoginPage(){
		List<LoginPagePO> list = new ArrayList<>();
		list = loginPageDAO.findAll();
		return list;
	}
}
