/**
 * 
 */
package com.sumavision.tetris.omms.software.service.deployment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 绑定部署id的进程<br/>
 * <b>作者:</b>jiajun<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月12日 下午4:13:16
 */
@Entity
@Table(name = "TETRIS_OMMS_PROCESS_DEPLOYMENT")
public class ProcessDeploymentPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 进程名称 */
	private String processId;
	
	/** 进程别名 */
	private String processName;
	
	/** 服务部署id */
	private Long serviceDeploymentId;
	
	/** 进程状态 */
	private ProcessDeploymentStatus status;
	
	/** 服务器id */
	private Long serverId;
	
	@Column(name = "PROCESS_ID")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Column(name = "SERVICE_DEPLOYMENT_ID")
	public Long getServiceDeploymentId() {
		return serviceDeploymentId;
	}

	public void setServiceDeploymentId(Long serviceDeploymentId) {
		this.serviceDeploymentId = serviceDeploymentId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public ProcessDeploymentStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessDeploymentStatus status) {
		this.status = status;
	}

	@Column(name = "ServerId")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
}
