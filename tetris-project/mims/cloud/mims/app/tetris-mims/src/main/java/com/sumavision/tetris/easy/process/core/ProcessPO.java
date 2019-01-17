package com.sumavision.tetris.easy.process.core;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程对象<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月24日 下午5:09:08
 */
@Entity
@Table(name = "TETRIS_PROCESS")
public class ProcessPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 用户定义流程id */
	private String processId;
	
	/** 流程名称 */
	private String name;
	
	/** 流程说明 */
	private String remarks;
	
	/** bpmn内容 */
	private String bpmn;
	
	/** 流程类型 */
	private ProcessType type;
	
	/** 临时配置文件位置 */
	private String path;
	
	public ProcessPO(){
		this.setUuid(new StringBufferWrapper().append("_").append(this.getUuid()).toString());
	}
	
	@Column(name = "PROCESS_ID")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BPMN", columnDefinition = "text")
	public String getBpmn() {
		return bpmn;
	}

	public void setBpmn(String bpmn) {
		this.bpmn = bpmn;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ProcessType getType() {
		return type;
	}

	public void setType(ProcessType type) {
		this.type = type;
	}

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
