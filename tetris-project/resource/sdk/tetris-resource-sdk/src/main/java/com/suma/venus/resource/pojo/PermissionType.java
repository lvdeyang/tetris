package com.suma.venus.resource.pojo;

public enum PermissionType {

	VOD("点播"),
	RECORD_LOCAL("本地录制"),
	RECORD_SERVER("本地录制"),
	DOWNLOAD("下载"),
	PTZ("云台");
	
	private String name;
	
	private PermissionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
}
