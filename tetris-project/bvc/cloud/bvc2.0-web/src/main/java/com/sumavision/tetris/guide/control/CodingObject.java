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
 * <b>日期：</b>2020年9月10日 下午1:49:20
 */
public enum CodingObject {
	
	H264("h264"),
	H265("h265"),
	MPEG2("mpeg2"),
	AVS("avs+"),
	PASSBY("passby");
	
	private String name;
	
	private CodingObject(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static CodingObject fromName(String name) throws Exception{
		CodingObject[] values = CodingObject.values();
		for (CodingObject codingObject : values) {
			if(codingObject.getName().equals(name)){
				return codingObject;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
