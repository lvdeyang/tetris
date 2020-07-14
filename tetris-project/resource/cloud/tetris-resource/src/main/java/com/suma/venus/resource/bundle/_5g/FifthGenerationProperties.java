package com.suma.venus.resource.bundle._5g;

import java.io.InputStream;
import java.util.Properties;

public class FifthGenerationProperties {

	private static FifthGenerationProperties instance;
	
	private Integer minPort;
	
	private Integer maxPort;
	
	public Integer getMinPort() {
		return minPort;
	}

	public Integer getMaxPort() {
		return maxPort;
	}

	private FifthGenerationProperties() throws Exception{
		InputStream in = null;
		Properties props = null;
		try{
			in = this.getClass().getClassLoader().getResourceAsStream("properties/5G.properties");
			props = new Properties();
			props.load(in);
			this.minPort = Integer.valueOf(props.getProperty("port.min"));
			this.maxPort = Integer.valueOf(props.getProperty("port.max"));
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(in != null) in.close();
			if(props != null) props.clear();
		}
		
	}
	
	public static FifthGenerationProperties getInstance() throws Exception{
		synchronized (FifthGenerationProperties.class) {
			if(instance == null){
				instance = new FifthGenerationProperties();
			}
			return instance;
		}
	}
	
}
