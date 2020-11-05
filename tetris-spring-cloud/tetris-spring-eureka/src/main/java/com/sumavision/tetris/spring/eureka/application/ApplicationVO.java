package com.sumavision.tetris.spring.eureka.application;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ApplicationVO extends AbstractBaseVO<ApplicationVO, ApplicationPO>{

	private String name;
	
	private String instanceId;
	
	private String ip;
	
	private String port;
	
	private String securePort;
	
	private String status;
	
	private String gadgetPort;
	
	public String getName() {
		return name;
	}

	public ApplicationVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public ApplicationVO setInstanceId(String instanceId) {
		this.instanceId = instanceId;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public ApplicationVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getPort() {
		return port;
	}

	public ApplicationVO setPort(String port) {
		this.port = port;
		return this;
	}

	public String getSecurePort() {
		return securePort;
	}

	public ApplicationVO setSecurePort(String securePort) {
		this.securePort = securePort;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ApplicationVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getGadgetPort() {
		return gadgetPort;
	}

	public ApplicationVO setGadgetPort(String gadgetPort) {
		this.gadgetPort = gadgetPort;
		return this;
	}

	@Override
	public ApplicationVO set(ApplicationPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setInstanceId(entity.getInstanceId())
			.setIp(entity.getIp())
			.setPort(entity.getPort())
			.setSecurePort(entity.getSecurePort())
			.setStatus(entity.getStatus().toString())
			.setGadgetPort(entity.getGadgetPort());
		return this;
	}

}
