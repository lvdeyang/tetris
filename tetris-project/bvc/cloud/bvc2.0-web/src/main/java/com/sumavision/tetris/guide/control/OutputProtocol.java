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
 * <b>日期：</b>2020年9月22日 下午2:56:30
 */
public enum OutputProtocol {
	UDP("udp");
	
	private String name;
	
	private OutputProtocol(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static OutputProtocol fromName(String name) throws Exception{
		OutputProtocol[] values = OutputProtocol.values();
		for (OutputProtocol outputProtocol : values) {
			if(outputProtocol.getName().equals(name)){
				return outputProtocol;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
