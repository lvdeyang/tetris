package com.sumavision.tetris.easy.process.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.commons.constant.DataType;
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
	
	/** 数据类型 */
	private DataType dataType;

	/** 变量默认值 */
	private String defaultValue;
	
	/** 值引用表达式 */
	private String expressionValue;
	
	/** 流程id */
	private Long processId;
	
	/** 是否自动生成 */
	private Boolean autoGeneration;
	
	@Column(name = "PRIMARY_KEY")
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "DATA_TYPE")
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Column(name = "DEFAULT_VALUE")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Column(name = "EXPRESSION_VALUE")
	public String getExpressionValue() {
		return expressionValue;
	}

	public void setExpressionValue(String expressionValue) {
		this.expressionValue = expressionValue;
	}

	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
	@Column(name = "AUTO_GENERATION")
	public Boolean getAutoGeneration() {
		return autoGeneration;
	}

	public void setAutoGeneration(Boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
	}

	/**
	 * 从模板变量中复制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 下午3:10:17
	 * @param Long processId 目标流程id
	 * @return ProcessVariablePO 复制后的变量
	 */
	public ProcessVariablePO copy(Long processId){
		ProcessVariablePO entity = new ProcessVariablePO();
		entity.setName(this.getName());
		entity.setPrimaryKey(this.getPrimaryKey());
		entity.setDataType(this.getDataType());
		entity.setDefaultValue(this.getDefaultValue());
		entity.setExpressionValue(this.getExpressionValue());
		entity.setAutoGeneration(true);
		entity.setProcessId(processId);
		return entity;
	}
	
}
