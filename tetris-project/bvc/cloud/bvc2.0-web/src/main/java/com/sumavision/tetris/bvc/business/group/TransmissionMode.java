package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 发流方式：单播【|组播】 
 * @author lvdeyang 
 * @date 2018年7月31日 下午1:30:51 
 */
public enum TransmissionMode {

	UNICAST("单播");
	//MULTICAST("组播");
	
	private String name;
	
	private TransmissionMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取流传输类型 
	 * @param name
	 * @throws Exception
	 * @return TransmissionMode 流传输类型
	 */
	public static TransmissionMode fromName(String name) throws Exception{
		TransmissionMode[] values = TransmissionMode.values();
		for(TransmissionMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
