package com.sumavision.tetris.mims.config.server;

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
	
	private String ftpIp;
	
	private String ftpPort;
	
	private String ftpUsername;
	
	private String ftpPassword;
	
    private String omcftpIp;
	
	private String omcftpPort;
	
	private String omcftpUsername;
	
	private String omcftpPassword;

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

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getOmcftpIp() {
		return omcftpIp;
	}

	public void setOmcftpIp(String omcftpIp) {
		this.omcftpIp = omcftpIp;
	}

	public String getOmcftpPort() {
		return omcftpPort;
	}

	public void setOmcftpPort(String omcftpPort) {
		this.omcftpPort = omcftpPort;
	}

	public String getOmcftpUsername() {
		return omcftpUsername;
	}

	public void setOmcftpUsername(String omcftpUsername) {
		this.omcftpUsername = omcftpUsername;
	}

	public String getOmcftpPassword() {
		return omcftpPassword;
	}

	public void setOmcftpPassword(String omcftpPassword) {
		this.omcftpPassword = omcftpPassword;
	}
	
	
}
