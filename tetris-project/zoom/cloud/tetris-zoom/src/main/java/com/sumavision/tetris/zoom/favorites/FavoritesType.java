package com.sumavision.tetris.zoom.favorites;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum FavoritesType {

	ZOOM_CODE("会议号码");
	
	private String name;
	
	private FavoritesType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static FavoritesType fromName(String name) throws Exception{
		FavoritesType[] values = FavoritesType.values();
		for(FavoritesType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
