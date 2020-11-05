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
 * <b>日期：</b>2020年9月16日 上午11:43:30
 */
public enum RcMode {
	VBR("vbr"),
	CBR("cbr");
	
	private String name;
	
	private RcMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static RcMode fromName(String name) throws Exception{
		RcMode[] values = RcMode.values();
		for (RcMode rcMode : values) {
			if(rcMode.getName().equals(name)){
				return rcMode;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
