package com.suma.venus.resource.base.bo;

public class DeviceInfoParam {

	private String device_model;
	
	private String device_version;

	private DeviceAddrBO device_addr;
	
	public DeviceInfoParam() {
	}

	public DeviceInfoParam(String device_model, String device_version,
			String device_ip, Integer device_port) {
		this.device_model = device_model;
		this.device_version = device_version;
		this.device_addr = new DeviceAddrBO(device_ip, device_port);
	}
	
	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevice_version() {
		return device_version;
	}

	public void setDevice_version(String device_version) {
		this.device_version = device_version;
	}

	public DeviceAddrBO getDevice_addr() {
		return device_addr;
	}

	public void setDevice_addr(DeviceAddrBO device_addr) {
		this.device_addr = device_addr;
	}

}
