package com.sumavision.tetris.capacity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:capacity.properties")
public class CapacityProps {

	@Value("${ip}")
	private String ip;
	
	@Value("${port}")
	private Long port;
	
	@Value("${zuul.ip}")
	private String zuulIp;
	
	@Value("${zuul.port}")
	private String zuulPort;

	public String getIp() {
		return ip;
	}

	public Long getPort() {
		return port;
	}

	public String getZuulIp() {
		return zuulIp;
	}

	public String getZuulPort() {
		return zuulPort;
	}
	
}
