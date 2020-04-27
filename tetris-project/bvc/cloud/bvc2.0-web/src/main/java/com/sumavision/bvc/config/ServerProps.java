package com.sumavision.bvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties
public class ServerProps {

	@Value("${spring.application.name}")
	private String id;
	
	@Value("${spring.cloud.client.ipAddress}")
	private String ip;
	
	@Value("${server.port}")
	private String port;
	
	/**
	 * 该字段配置指挥业务中的文字是“指挥”还是“会议”
	 */
	@Value("${command.commandString}")
	private String commandString;
	
	/**
	 * 创建本地预览的模式，0关闭，1使用摄像头，2使用绑定的编码器
	 */
	@Value("${command.localPreviewMode}")
	private int localPreviewMode;

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

	public String getCommandString() {
		return commandString;
	}

	public void setCommandString(String commandString) {
		this.commandString = commandString;
	}

	public int getLocalPreviewMode() {
		return localPreviewMode;
	}

	public void setLocalPreviewMode(int localPreviewMode) {
		this.localPreviewMode = localPreviewMode;
	}
	
}
