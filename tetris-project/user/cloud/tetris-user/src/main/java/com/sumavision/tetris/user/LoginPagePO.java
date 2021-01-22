/**
 * 
 */
package com.sumavision.tetris.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 登陆页面表<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月13日 下午3:11:54
 */

@Entity
@Table(name = "TETRIS_USER_LOGIN_PAGE")
public class LoginPagePO extends AbstractBasePO {
	
	private static final long serialVersionUID = 1L;

	/** 页面名 */
	private String name;
	
	/** 页面模板 */
	private String tpl;
	
	/** 是否使用*/
	private boolean isCurrent;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Lob
	@Column(name = "TPL",columnDefinition = "LONGTEXT")
	public String getTpl() {
		return tpl;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	@Column(name = "ISCUREENT")
	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
}
