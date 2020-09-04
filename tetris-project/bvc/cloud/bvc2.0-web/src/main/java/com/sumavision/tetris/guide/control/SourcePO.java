/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月2日 下午7:35:14
 */
@Entity
@Table(name = "TETRIS_SOURCE_PO")
public class SourcePO extends AbstractBasePO{

	/** 源编号 */
	private Long id;
	
	/** 源类型 */
	private String sourceType;
	
	/** 源名称 */
	private String sourceName;   
	
	/** 源地址 */
	private String sourceAddress;
	
	/** 导播任务编号 */
	private Long taskNumber;

	@Column(name = "SOURCE_NUMBER")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "SOURCE_TYPE")
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "SOURCE_NAME")
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Column(name = "SOURCE_ADDRESS")
	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}
	
	@Column(name = "TASK_NUMBER")
	public Long getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Long taskNumber) {
		this.taskNumber = taskNumber;
	}

}
