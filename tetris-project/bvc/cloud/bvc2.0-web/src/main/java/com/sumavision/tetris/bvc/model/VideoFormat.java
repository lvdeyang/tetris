package com.sumavision.tetris.bvc.model;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum VideoFormat {

	H_265("h265"),
	H_264("h264");
	
	private String name;
	
	private VideoFormat(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static VideoFormat fromName(String name) throws Exception{
		VideoFormat[] values = VideoFormat.values();
		for(VideoFormat value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
