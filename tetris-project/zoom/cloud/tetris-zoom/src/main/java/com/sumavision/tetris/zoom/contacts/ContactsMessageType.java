package com.sumavision.tetris.zoom.contacts;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ContactsMessageType {

	TEXT("文字"),
	FILE("文件");
	
	private String name;
	
	private ContactsMessageType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ContactsMessageType fromName(String name) throws Exception{
		ContactsMessageType[] values = ContactsMessageType.values();
		for(ContactsMessageType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
