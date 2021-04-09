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
	
	/** 2个ntp服务的IP和端口 */
	@Value("${command.ntp1IP}")
	private String ntp1IP;
	
	@Value("${command.ntp1Port}")
	private String ntp1Port;
	
	@Value("${command.ntp2IP}")
	private String ntp2IP;
	
	@Value("${command.ntp2Port}")
	private String ntp2Port;

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

	public String getNtp1IP() {
		return ntp1IP;
	}

	public void setNtp1IP(String ntp1ip) {
		ntp1IP = ntp1ip;
	}

	public String getNtp1Port() {
		return ntp1Port;
	}

	public void setNtp1Port(String ntp1Port) {
		this.ntp1Port = ntp1Port;
	}

	public String getNtp2IP() {
		return ntp2IP;
	}

	public void setNtp2IP(String ntp2ip) {
		ntp2IP = ntp2ip;
	}

	public String getNtp2Port() {
		return ntp2Port;
	}

	public void setNtp2Port(String ntp2Port) {
		this.ntp2Port = ntp2Port;
	}
	
}
