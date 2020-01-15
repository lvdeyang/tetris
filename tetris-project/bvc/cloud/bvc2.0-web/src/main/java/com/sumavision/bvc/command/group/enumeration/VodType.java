package com.sumavision.bvc.command.group.enumeration;

public enum VodType {

	USER("用户", "点播用户"),
	USER_ONESELF("用户观看自己", "本地视频"),
	DEVICE("设备", "点播设备");
	
	private String name;
	
	private String code;
	
	private VodType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

}
