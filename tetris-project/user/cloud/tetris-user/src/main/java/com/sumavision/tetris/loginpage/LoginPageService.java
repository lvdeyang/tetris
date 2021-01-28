/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.List;

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
	
	@Autowired
	private LoginPageQuery loginPageQuery;
	
	/**
	 * 添加登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午4:08:02
	 * @param name
	 * @param remark
	 * @param isCurrent
	 * @param tpl
	 * @return
	 */
	public LoginPagePO addLoginPage(String name,String remark,boolean isCurrent,String tpl){
		LoginPagePO loginPagePO = new LoginPagePO();
		loginPagePO.setName(name);
		loginPagePO.setRemark(remark);
		loginPagePO.setIsCurrent(isCurrent);
		loginPagePO.setTpl(tpl);
		loginPageDAO.save(loginPagePO);
		return loginPagePO;
	}
	
	/**
	 * 删除登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午4:08:26
	 * @param id
	 * @return
	 */
	public Object deleteLoginPage(long id){
		loginPageDAO.deleteById(id);
		return null;
	}
	
	/**
	 * 使用登陆页面<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午4:08:41
	 * @param id
	 * @return
	 */
	public Object useLoginPage(long id){
		LoginPagePO loginPagePO=loginPageDAO.findById(id);
		List<LoginPagePO> list = new ArrayList<>();
		if(loginPagePO.getIsCurrent()==true){
			loginPagePO.setIsCurrent(false);
		}else{
			
			//使用当前页面后将其他页面使用状态置为未使用
			list = loginPageDAO.findAll();
			for(LoginPagePO each:list){
				each.setIsCurrent(false);
			}
			loginPagePO.setIsCurrent(true);
		}
		loginPageDAO.saveAll(list);
		loginPageDAO.save(loginPagePO);
		return null;
	}
	
	public Object editLoginPage(long id,String name,String remark,String tpl){
		LoginPagePO loginPagePO=loginPageDAO.findById(id);
		loginPagePO.setName(name);
		loginPagePO.setRemark(remark);
		loginPagePO.setTpl(tpl);
		loginPageDAO.save(loginPagePO);
		return loginPagePO;
	}
}
