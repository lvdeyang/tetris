/**
 * 
 */
package com.sumavision.tetris.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 变量类型<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月13日 下午3:12:46
 */

@Entity
@Table(name = "TETRIS_USER_VARIABLE_TYPE")
public class VariableTypePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	/** 变量类型名*/
	private String name;
	
	/** 变量key*/
	private String variableKey;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "VARIABLE_KEY")
	public String getVariableKey() {
		return variableKey;
	}
	public void setVariableKey(String variableKey) {
		this.variableKey = variableKey;
	}
}
