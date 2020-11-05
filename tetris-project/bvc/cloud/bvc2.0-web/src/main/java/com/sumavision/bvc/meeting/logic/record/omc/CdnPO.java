package com.sumavision.bvc.meeting.logic.record.omc;

import java.util.Properties;

public class CdnPO {
	
	private String useCdn = "true";
	
	private String useMsu = "true";
	
	private String msuPlayIp;
	
	private String msuPlayPort;

	private String ip;
	
	private String playIp;
	
	private String devicePort;

	private String playPort;
	
	private String udpStartPort;
	
	private String udpStopPort;
	
	private String ciAdapterIp;
	
	private String ciAdapterDevicePort;
	
	private String ciAdapterValueName;
	
	public static CdnPO getFromOmcConfigFile(){
		try {
			Properties prop = new Properties();
			prop.load(CdnPO.class.getClassLoader().getResourceAsStream("omc.properties"));
			
			CdnPO po = new CdnPO();
			po.setUseCdn(prop.getProperty("useCdn"));
			po.setUseMsu(prop.getProperty("useMsu"));
			po.setMsuPlayIp(prop.getProperty("msu.playIp"));
			po.setMsuPlayPort(prop.getProperty("msu.playPort"));
			po.setIp(prop.getProperty("cdn.ip"));
			po.setPlayIp(prop.getProperty("cdn.playIp"));
			po.setDevicePort(prop.getProperty("cdn.devicePort"));
			po.setPlayPort(prop.getProperty("cdn.playPort"));
			po.setUdpStartPort(prop.getProperty("cdn.udpStartPort"));
			po.setUdpStopPort(prop.getProperty("cdn.udpStopPort"));
			po.setCiAdapterIp(prop.getProperty("ciadapter.ip"));
			po.setCiAdapterDevicePort(prop.getProperty("ciadapter.devicePort"));
			po.setCiAdapterValueName(prop.getProperty("ciadapter.valueName"));
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

	public String getUdpStartPort() {
		return udpStartPort;
	}

	public void setUdpStartPort(String udpStartPort) {
		this.udpStartPort = udpStartPort;
	}

	public String getUdpStopPort() {
		return udpStopPort;
	}

	public void setUdpStopPort(String udpStopPort) {
		this.udpStopPort = udpStopPort;
	}

	public String getPlayPort() {
		return playPort;
	}

	public void setPlayPort(String playPort) {
		this.playPort = playPort;
	}

	public String getCiAdapterIp() {
		return ciAdapterIp;
	}

	public void setCiAdapterIp(String ciAdapterIp) {
		this.ciAdapterIp = ciAdapterIp;
	}

	public String getCiAdapterDevicePort() {
		return ciAdapterDevicePort;
	}

	public void setCiAdapterDevicePort(String ciAdapterDevicePort) {
		this.ciAdapterDevicePort = ciAdapterDevicePort;
	}

	public String getCiAdapterValueName() {
		return ciAdapterValueName;
	}

	public void setCiAdapterValueName(String ciAdapterValueName) {
		this.ciAdapterValueName = ciAdapterValueName;
	}

	public String getPlayIp() {
		return playIp;
	}

	public void setPlayIp(String playIp) {
		this.playIp = playIp;
	}

	public String getUseCdn() {
		return useCdn;
	}

	public void setUseCdn(String useCdn) {
		this.useCdn = useCdn;
	}

	public String getUseMsu() {
		return useMsu;
	}

	public void setUseMsu(String useMsu) {
		this.useMsu = useMsu;
	}

	public String getMsuPlayIp() {
		return msuPlayIp;
	}

	public void setMsuPlayIp(String msuPlayIp) {
		this.msuPlayIp = msuPlayIp;
	}

	public String getMsuPlayPort() {
		return msuPlayPort;
	}

	public void setMsuPlayPort(String msuPlayPort) {
		this.msuPlayPort = msuPlayPort;
	}
}
