package com.sumavision.signal.bvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:properties/capacity.properties")
public class CapacityProps {

	@Value("$(capacity.ip)")
	private String capacityIp;
	
	@Value("$(capacity.port)")
	private String capacityPort;
	
	@Value("$(capacity.ip)")
	private String transcodeIp;
	
	@Value("$(capacity.port)")
	private String transcodePort;
	
	@Value("$(appId)")
	private String appId;
	
	@Value("$(appSecret)")
	private String appSecret;
	
	/** 为了测试加的 */
	@Value("$(netId)")
	private String netId;

	/** 为了测试加的 */
	@Value("$(netIp)")
	private String netIp;

	public String getCapacityIp() {
		return capacityIp;
	}

	public String getCapacityPort() {
		return capacityPort;
	}

	public String getTranscodeIp() {
		return transcodeIp;
	}

	public String getTranscodePort() {
		return transcodePort;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getNetId() {
		return netId;
	}

	public String getNetIp() {
		return netIp;
	} 
	
}
