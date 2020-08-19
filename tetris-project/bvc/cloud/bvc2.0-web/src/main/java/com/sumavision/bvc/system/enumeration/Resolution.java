package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 分辨率 
 * @author lvdeyang
 * @date 2018年7月31日 上午11:18:43 
 */
public enum Resolution {
	
	P1920X1200("1920x1200"),
	P1920X1080("1920x1080"),	
	//P1366X900("1366x900"),
	P1280X720("1280x720"),
	//P1024X768("1024x768"),
	P704X576("704x576"),
	P352X288("352x288"),
	P176X144("176x144");
	
	private String name;
	
	private Resolution(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public static Resolution fromName(String name) throws Exception{
		Resolution[] values = Resolution.values();
		for(Resolution value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
