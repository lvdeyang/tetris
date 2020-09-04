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
 * <b>日期：</b>2020年9月2日 下午7:47:36
 */
@Entity
@Table(name = "TETRIS_OUTPUT_SETTING_PO")
public class OutputSettingPO extends AbstractBasePO {
	
	/** 输出编号 */
	private Long id;
	
	/** 输出协议 */
	private String outputProtocol;
	
	/** 输出地址 */
	private String outputAddress;
	
	/** 导播任务编号 */
	private Long taskNumber;

	@Column(name = "OUTPUT_NUMBER")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "OUTPUT_PROTOCOL")
	public String getOutputProtocol() {
		return outputProtocol;
	}

	public void setOutputProtocol(String outputProtocol) {
		this.outputProtocol = outputProtocol;
	}
	
	@Column(name = "OUTPUT_ADDRESS")
	public String getOutputAddress() {
		return outputAddress;
	}

	public void setOutputAddress(String outputAddress) {
		this.outputAddress = outputAddress;
	}
	
	@Column(name = "TASK_NUMBER")
	public Long getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Long taskNumber) {
		this.taskNumber = taskNumber;
	}

}
