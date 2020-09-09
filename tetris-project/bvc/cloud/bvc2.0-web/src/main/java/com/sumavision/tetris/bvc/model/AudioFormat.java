package com.sumavision.tetris.bvc.model;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AudioFormat {

	PCMU("pcmu"), 
	PCMA("pcma"), 
	AAC("aac"),
	G729("g729"),
	MP2("mp2");
	
	private String name;
	
	private AudioFormat(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AudioFormat fromName(String name) throws Exception{
		AudioFormat[] values = AudioFormat.values();
		for(AudioFormat value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
