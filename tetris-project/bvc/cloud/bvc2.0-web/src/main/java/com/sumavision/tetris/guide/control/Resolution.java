/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月15日 上午10:54:54
 */
public enum Resolution {
	
	_3840_2160("3840x2160"),
	_1920_1080("1920x1080"),
	_1280_720("1280x720"),
	_720_576("720x576"),
	_640_480("640x480");
	
	private String name;
	
	private Resolution(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static Resolution fromName(String name) throws Exception{
		Resolution[] values = Resolution.values();
		for (Resolution resolution : values) {
			if(resolution.getName().equals(name)){
				return resolution;
			}
		}
		throw new ErrorTypeException("name", "分辨率错误");
	}
}
