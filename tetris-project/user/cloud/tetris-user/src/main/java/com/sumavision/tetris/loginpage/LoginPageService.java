/**
 * 
 */
package com.sumavision.tetris.loginpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月14日 下午3:27:44
 */
@Service
public class LoginPageService {
	
	@Autowired 
	private LoginPageDAO loginPageDAO;
	
	public LoginPagePO addLoginPage(String name,String remark,boolean isCurrent,String tpl){
		LoginPagePO loginPagePO = new LoginPagePO();
		loginPagePO.setName(name);
		loginPagePO.setRemark(remark);
		loginPagePO.setIsCurrent(isCurrent);
		loginPagePO.setTpl(tpl);
		loginPageDAO.save(loginPagePO);
		return loginPagePO;
	}
	
	public Object deleteLoginPage(long id){
		loginPageDAO.deleteById(id);
		return null;
	}
	
	public Object useLoginPage(long id){
		LoginPagePO loginPagePO=loginPageDAO.findById(id);
		if(loginPagePO.getIsCurrent()==true){
			loginPagePO.setIsCurrent(false);
		}else{
			loginPagePO.setIsCurrent(true);
		}
		loginPageDAO.save(loginPagePO);
		return null;
	}
}
