package com.sumavision.tetris.bvc.model;


import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum UsageType {

	VOD("点播系统"),
	BACKPACK_5G("5G背包"),
	COMMAND("指控终端"),
	DEVICE_GROUP("BVC会控"),
	STB("机顶盒终端"),
	MOBILE("手机终端");
	
	private String name;
	
	private UsageType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static UsageType fromName(String name) throws Exception{
		UsageType[] values = UsageType.values();
		for(UsageType value:values){
			if (value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
