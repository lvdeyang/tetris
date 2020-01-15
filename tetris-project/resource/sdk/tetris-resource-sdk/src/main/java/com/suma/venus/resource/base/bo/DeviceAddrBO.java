package com.suma.venus.resource.base.bo;

public class DeviceAddrBO {
	
	private String device_ip;
	
	private Integer device_port;
	
	public DeviceAddrBO(){
	}
	
	public DeviceAddrBO(String device_ip, Integer device_port) {
		this.device_ip = device_ip;
		this.device_port = device_port;
	}
	
	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public Integer getDevice_port() {
		return device_port;
	}

	public void setDevice_port(Integer device_port) {
		this.device_port = device_port;
	}

}
