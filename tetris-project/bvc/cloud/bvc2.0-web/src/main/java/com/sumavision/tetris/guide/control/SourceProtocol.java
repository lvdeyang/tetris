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
 * <b>日期：</b>2020年9月28日 上午10:27:11
 */
public enum SourceProtocol {
	
	UDP_TS("udp_ts"),
	SRT("srt");
	
	private String name;
	
	private SourceProtocol(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static SourceProtocol fromName(String name) throws Exception{
		SourceProtocol[] values = SourceProtocol.values();
		for (SourceProtocol sourceProtocol : values) {
			if(sourceProtocol.getName().equals(name)){
				return sourceProtocol;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
