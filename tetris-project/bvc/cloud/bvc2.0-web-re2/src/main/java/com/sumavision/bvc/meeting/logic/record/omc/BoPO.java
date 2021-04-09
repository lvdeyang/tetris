package com.sumavision.bvc.meeting.logic.record.omc;

import java.util.Properties;

public class BoPO {
	
	/*
	 * 标识是否使用BO：建立直播栏目时是否与BO对接
	 */
	private String useBo = "true";

	private String ip;
	
	private String devicePort;
	
	public static BoPO getFromOmcConfigFile(){
		try {
			Properties prop = new Properties();
			prop.load(BoPO.class.getClassLoader().getResourceAsStream("omc.properties"));
			
			BoPO po = new BoPO();
			po.setIp(prop.getProperty("bo.ip"));
			po.setDevicePort(prop.getProperty("bo.devicePort"));
			po.setUseBo(prop.getProperty("useBo"));
			return po;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(String devicePort) {
		this.devicePort = devicePort;
	}

	public String getUseBo() {
		return useBo;
	}

	public void setUseBo(String useBo) {
		this.useBo = useBo;
	}
}
