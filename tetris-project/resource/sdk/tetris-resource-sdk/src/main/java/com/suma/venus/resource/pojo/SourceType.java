package com.suma.venus.resource.pojo;


public enum SourceType {

	FOLDER("文件夹"),
	DEVICE("设备");
	
	private String name;
	
	private SourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SourceType fromName(String name) throws Exception{
		SourceType[] values = SourceType.values();
		for(SourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		return null;
	}
	
}
