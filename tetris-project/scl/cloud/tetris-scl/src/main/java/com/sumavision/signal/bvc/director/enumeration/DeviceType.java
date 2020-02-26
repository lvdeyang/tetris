package com.sumavision.signal.bvc.director.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 设备类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月20日 下午1:36:23
 */
public enum DeviceType {

	FIVEG("5G背包", "5G"),
	PLAYER("播放器", "player");
	
	private String name;
	
	private String deviceModel;
	
	private DeviceType (String name, String deviceModel){
		this.name = name;
		this.deviceModel = deviceModel;
	}

	public String getName() {
		return name;
	}

	public String getDeviceModel() {
		return deviceModel;
	}
	
	public static final DeviceType fromDeviceModel(String deviceModel) throws Exception{
		DeviceType[] values = DeviceType.values();
		for(DeviceType value: values){
			if(value.getDeviceModel().equals(deviceModel)){
				return value;
			}
		}
		throw new ErrorTypeException("deviceModel", deviceModel);
	}
	
}
