/**
 * 
 */
package com.sumavision.tetris.loginpage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型表<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月13日 下午3:13:48
 */
@Entity
@Table(name = "TETRIS_USER_VARIABLE")
public class VariablePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 变量类型ID*/
	private String variableTypeId;
	
	/** 取值，text或img（base64编码）*/
	private String value;
	
	@Lob
	@Column(name = "VALUE",columnDefinition = "LONGTEXT")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name = "VARIABLE_TYPE_ID")
	public String getVariableTypeId() {
		return variableTypeId;
	}
	public void setVariableTypeId(String variableTypeId) {
		this.variableTypeId = variableTypeId;
	}
	
}
