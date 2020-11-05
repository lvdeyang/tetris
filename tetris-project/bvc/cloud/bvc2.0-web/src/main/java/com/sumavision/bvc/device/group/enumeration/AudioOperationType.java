package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 议程音频操作方式 
 * @author lvdeyang
 * @date 2018年8月4日 上午10:08:26 
 */
public enum AudioOperationType {

	CUSTOM("自定义"),
	KEEPSTATUS("保持状态"),
	MUTE("静音");
	
	private String name;
	
	private AudioOperationType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据议程音频类型名称获取类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return AgendaAudioType 议程音频类型
	 */
	public static final AudioOperationType fromName(String name) throws Exception{
		AudioOperationType[] values = AudioOperationType.values();
		for(AudioOperationType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
