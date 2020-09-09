package com.sumavision.tetris.easy.process.core;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 记录流程发布情况<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月4日 上午9:27:34
 */
@Entity
@Table(name = "TETRIS_PROCESS_DEPLOYMENT_PERMISSION")
public class ProcessDeploymentPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 流程定义id */
	private Long processId;
	
	/** 流程名称 */
	private String name;
	
	/** 流程说明 */
	private String remarks;
	
	/** bpmn内容 */
	private String bpmn;
	
	/** 流程发布id */
	private String deploymentId;
	
	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
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

	@Column(name = "DEPLOYMENT_ID")
	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	
}
