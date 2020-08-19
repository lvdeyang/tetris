package com.sumavision.tetris.easy.process.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程发布的版本与使用情况<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月4日 上午9:47:00
 */
@Entity
@Table(name = "TETRIS_PROCESS_INSTANCE_DEPLOYMENT_PERMISSION")
public class ProcessInstanceDeploymentPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 流程实例id */
	private String processInstanceId;
	
	/** 流程发布id */
	private String deploymentId;

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "DEPLOYMENT_ID")
	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	
}
