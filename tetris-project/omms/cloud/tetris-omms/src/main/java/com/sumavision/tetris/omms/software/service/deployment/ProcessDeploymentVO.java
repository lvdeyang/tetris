/**
 * 
 */
package com.sumavision.tetris.omms.software.service.deployment;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;


public class ProcessDeploymentVO extends AbstractBaseVO<ProcessDeploymentVO, ProcessDeploymentPO>{

	/** 进程名称 */
	private String processId;
	
	/** 进程别名 */
	private String processName;
	
	/** 服务部署id */
	private Long serviceDeploymentId;
	
	/** 进程状态 */
	private String status;
	
	/** 服务器id */
	private Long serverId;
	
	public String getProcessId() {
		return processId;
	}

	public ProcessDeploymentVO setProcessId(String processId) {
		this.processId = processId;
		return this;
	}

	public String getProcessName() {
		return processName;
	}
	
	public ProcessDeploymentVO setProcessName(String processName) {
		this.processName = processName;
		return this;
	}

	public Long getServiceDeploymentId() {
		return serviceDeploymentId;
	}

	public ProcessDeploymentVO setServiceDeploymentId(Long serviceDeploymentId) {
		this.serviceDeploymentId = serviceDeploymentId;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ProcessDeploymentVO setStatus(String status) {
		this.status = status;
		return this;
	}
	
	public Long getServerId() {
		return serverId;
	}

	public ProcessDeploymentVO setServerId(Long serverId) {
		this.serverId = serverId;
		return this;
	}

	@Override
	public ProcessDeploymentVO set(ProcessDeploymentPO entity) throws Exception {
		this.setId(entity.getId())
			.setProcessId(entity.getProcessId())
			.setProcessName(entity.getProcessName())
			.setServiceDeploymentId(entity.getServiceDeploymentId())
			.setStatus(entity.getStatus()!= null ? entity.getStatus().getName(): null)
			.setServerId(entity.getServerId());
		return this;
	}

}
