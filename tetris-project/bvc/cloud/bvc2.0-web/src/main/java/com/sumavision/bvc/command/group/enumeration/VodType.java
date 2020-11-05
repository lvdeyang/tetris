package com.sumavision.bvc.command.group.enumeration;

public enum VodType {

	USER("用户", "点播用户"),//本地看本地用户
	USER_ONESELF("用户观看自己", "本地视频"),
	DEVICE("设备", "点播设备"),//本地看本地设备
	LOCAL_SEE_OUTER_DEVICE("本地点播外部设备", "点播设备"),
	LOCAL_SEE_OUTER_USER("本地点播外部用户", "点播用户"),
	FILE("文件", "点播文件");//专用于标识上屏任务
	
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
