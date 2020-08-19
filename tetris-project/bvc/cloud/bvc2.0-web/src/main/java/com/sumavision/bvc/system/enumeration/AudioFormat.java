package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 音频编码格式 
 * @author lvdeyang
 * @date 2018年7月25日 下午2:53:31 
 */
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
	
	/**
	 * @Title: 根据名称获取音频编码格式 
	 * @param name
	 * @return AudioFormat 音频编码格式 
	 */
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
