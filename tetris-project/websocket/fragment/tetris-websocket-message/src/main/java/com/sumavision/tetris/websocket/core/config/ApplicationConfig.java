package com.sumavision.tetris.websocket.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties
public class ApplicationConfig{

	@Value("${spring.application.name}")
	private String id;
	
	@Value("${spring.cloud.client.ipAddress}")
	private String ip;
	
	@Value("${server.port}")
	private String port;
	
	@Value("${eureka.client.serviceUrl.defaultZone}")
	private String eurekaUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getEurekaUrl() {
		return eurekaUrl;
	}

	public void setEurekaUrl(String eurekaUrl) {
		this.eurekaUrl = eurekaUrl;
	}
	
}
