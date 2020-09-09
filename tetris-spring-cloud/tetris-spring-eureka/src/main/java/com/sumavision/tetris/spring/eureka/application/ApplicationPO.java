package com.sumavision.tetris.spring.eureka.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 微服务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月20日 下午5:18:22
 */
@Entity
@Table(name = "TETRIS_EUREKA_APPLICATION", uniqueConstraints = {@UniqueConstraint(columnNames={"INSTANCE_ID"})})
public class ApplicationPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 微服务id */
	private String name;
	
	/** 微服务实例id */
	private String instanceId;
	
	/** 实例ip */
	private String ip;
	
	/** http端口 */
	private String port;
	
	/** https端口 */
	private String securePort;
	
	/** 服务状态 */
	private ApplicationStatus status;
	
	/** 默认 8910 */
	private String gadgetPort;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "INSTANCE_ID")
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "SECURE_PORT")
	public String getSecurePort() {
		return securePort;
	}

	public void setSecurePort(String securePort) {
		this.securePort = securePort;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	@Column(name = "GADGET_PORT")
	public String getGadgetPort() {
		return gadgetPort;
	}

	public void setGadgetPort(String gadgetPort) {
		this.gadgetPort = gadgetPort;
	}
	
}
