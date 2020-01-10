package com.sumavision.tetris.spring.eureka.application;

public class ApplicationVO{

	private Long id;
	
	private String uuid;
	
	private String name;
	
	private String instanceId;
	
	private String ip;
	
	private String port;
	
	private String securePort;
	
	private String status;
	
	private String gadgetPort;
	
	public Long getId() {
		return id;
	}

	public ApplicationVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ApplicationVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

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

}
