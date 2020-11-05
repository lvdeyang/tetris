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
 * <b>日期：</b>2020年9月10日 下午2:28:27
 */
public enum CodingFormat {
	
	AAC("aac"),
	MP2("mp2"),
	MP3("mp3"),
	DOLBY("dolby"),
	PASSBY("passby");
	
	private String name;
	
	private CodingFormat(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static CodingFormat fromName(String name) throws Exception{
		CodingFormat[] values = CodingFormat.values();
		for (CodingFormat codingFormat : values) {
			if(codingFormat.getName().equals(name)){
				return codingFormat;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
