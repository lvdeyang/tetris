package com.sumavision.tetris.easy.process.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程变量<br/>
 * <p>
 * 	主要存储附加变量，不包含服务参数和内置变量<br/>
 *  附加变量可以用于分支判断<br/>
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月3日 上午9:41:23
 */
@Entity
@Table(name = "TETRIS_PROCESS_VARIABLE")
public class ProcessVariablePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 变量键 */
	private String primaryKey;
	
	/** 变量名称 */
	private String name;

	/** 变量默认值 */
	private String defaultValue;
	
	/** 流程id */
	private Long processId;
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
}
