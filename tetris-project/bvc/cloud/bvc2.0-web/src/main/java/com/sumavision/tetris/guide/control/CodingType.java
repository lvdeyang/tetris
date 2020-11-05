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
 * <b>日期：</b>2020年9月10日 下午2:31:00
 */
public enum CodingType {
	MPEG4_ACC_LC("mpeg4-aac-lc"),
	MPEG4_HE_AAC_LC("mpeg4-he-aac-lc"),
	MPEG4_HE_AAC_V2_1C("mpeg4-he-aac-v2-lc"),
	AC3("ac3"),
	EAC3("eac3");
	
	private String name;
	
	private CodingType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static CodingType fromName(String name) throws Exception{
		CodingType[] values = CodingType.values();
		for (CodingType codingType : values) {
			if(codingType.getName().equals(name)){
				return codingType;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
