package com.sumavision.tetris.spring.eureka.config;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component  
@ConfigurationProperties
public class ApplicationYml {

	@Value("${eureka.client.serviceUrl.defaultZone}")
	private String eurekaUrl;
	
	private URL url;
	
	private void buildUrl() throws Exception{
		if(this.url == null){
			this.url = new URL(this.getEurekaUrl());
		}
	}
	
	public String getEurekaUrl() {
		return eurekaUrl;
	}

	public void setEurekaUrl(String eurekaUrl) {
		this.eurekaUrl = eurekaUrl;
	}
	
	public String getIp() throws Exception{
		this.buildUrl();
		return this.url.getHost();
	}
	
	public int getPort() throws Exception{
		this.buildUrl();
		return this.url.getPort();
	}
	
	public String getBaseUrl() throws Exception{
		this.buildUrl();
		return new StringBufferWrapper().append(this.url.getProtocol())
										.append("://")
										.append(this.url.getHost())
										.append(":")
										.append(this.url.getPort())
										.toString();
	}
	
}
