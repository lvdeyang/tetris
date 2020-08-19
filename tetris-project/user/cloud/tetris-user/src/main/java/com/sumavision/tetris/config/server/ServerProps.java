package com.sumavision.tetris.config.server;

import java.io.InputStream;
import java.util.Properties;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}
	
	public String getIpFromProperties() throws Exception{
		InputStream in = null;
		try{
			in = ServerProps.class.getClassLoader().getResourceAsStream("serverIp.properties");
			Properties properties = new Properties();
			properties.load(in);
			return properties.getProperty("ip");
		}finally{
			if(in != null) in.close();
		}
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
	
}
