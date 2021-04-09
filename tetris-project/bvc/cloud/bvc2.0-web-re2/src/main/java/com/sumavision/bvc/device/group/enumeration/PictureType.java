package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 描述合屏分屏的画面类型 
 * @author lvdeyang
 * @date 2018年7月31日 下午8:23:28 
 */
public enum PictureType {

	POLLING("轮询"),
	STATIC("单画面");
	
	private String name;
	
	private PictureType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取合屏分屏的画面类型 <br/>
	 * @param name 
	 * @return PictureType 合屏分屏的画面类型 
	 */
	public static PictureType fromName(String name) throws Exception{
		PictureType[] values = PictureType.values();
		for(PictureType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
