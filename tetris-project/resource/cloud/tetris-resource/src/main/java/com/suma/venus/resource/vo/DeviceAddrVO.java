package com.suma.venus.resource.vo;

public class DeviceAddrVO {
	private String deviceIp;
	
	private Integer devicePort;
	
	public DeviceAddrVO(){
	}

	public DeviceAddrVO(String deviceIp, Integer devicePort) {
		super();
		this.deviceIp = deviceIp;
		this.devicePort = devicePort;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}
	
}
