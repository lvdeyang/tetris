package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_REPEATER_TASK", uniqueConstraints={@UniqueConstraint(columnNames = {"ip", "dstIp", "dstPort"})})
public class TaskPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	private Long mappingId;
	
	private String taskId;
	
	/** 集群ip */
	private String ip;
	
	/** 任务实际输出ip */
	private String outIp;
	
	/** 描述任务信息 */
	/** 源ip */
	private String srcIp;
	
	/** 源port */
	private String srcPort;
	
	/** 目的ip */
	private String dstIp;
	
	/** 目的port */
	private String dstPort;
	
	/** 任务状态 */
	private String status;
	
	/** 任务状态信息 */
	private String message;

	public Long getMappingId() {
		return mappingId;
	}

	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOutIp() {
		return outIp;
	}

	public void setOutIp(String outIp) {
		this.outIp = outIp;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
