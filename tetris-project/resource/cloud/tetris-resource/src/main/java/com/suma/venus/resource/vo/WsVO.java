package com.suma.venus.resource.vo;

/**
 * ws设备信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月12日 上午9:08:05
 */
public class WsVO {

	private String deviceIp;
	
	private String type;

	public String getDeviceIp() {
		return deviceIp;
	}

	public WsVO setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
		return this;
	}

	public String getType() {
		return type;
	}

	public WsVO setType(String type) {
		this.type = type;
		return this;
	}
	
}
