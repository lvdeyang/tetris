/**
 * 
 */
package com.sumavision.tetris.loginpage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 页面变量映射表<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月13日 下午3:11:54
 */

@Entity
@Table(name = "TETRIS_USER_PAGE_VARIABLE")
public class PageVariablePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 页面Id */
	private long loginPageId;
	
	/** 变量Id */
	private String variableId;
	

	@Column(name = "LOGINPAGE_ID")
	public long getLoginPageId() {
		return loginPageId;
	}

	public void setLoginPageId(long loginPageId) {
		this.loginPageId = loginPageId;
	}

	@Column(name = "VARIABLE_ID")
	public String getVariableId() {
		return variableId;
	}

	public void setVariableId(String variableId) {
		this.variableId = variableId;
	}




}
