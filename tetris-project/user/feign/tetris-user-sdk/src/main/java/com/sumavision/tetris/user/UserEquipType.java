package com.sumavision.tetris.user;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum UserEquipType {
	WEB("平台页面", false),
	PUSH("播发终端", false),
	MOBILE("移动终端", false),
	QT("QT终端", true);
	
	private String name;
	
	private boolean show;
	
	private UserEquipType(String name, boolean show){
		this.name = name;
		this.show = show;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isShow() {
		return this.show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public static UserEquipType fromName(String name) throws Exception{
		UserEquipType[] values = UserEquipType.values();
		for(UserEquipType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
