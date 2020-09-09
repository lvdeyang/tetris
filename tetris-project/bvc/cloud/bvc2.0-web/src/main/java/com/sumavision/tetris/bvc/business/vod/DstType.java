package com.sumavision.tetris.bvc.business.vod;

public enum DstType {

	USER("用户"),
	DEVICE("设备");
	
	private String name;
	
	private DstType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
