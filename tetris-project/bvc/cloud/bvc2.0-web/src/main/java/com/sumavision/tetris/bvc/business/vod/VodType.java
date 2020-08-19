package com.sumavision.tetris.bvc.business.vod;

public enum VodType {

	USER("用户", "点播用户"),
	USER_ONESELF("本地", "本地视频"),//本地看本地用户
	DEVICE("设备", "点播设备"),
	FILE("文件", "点播文件");
	
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
