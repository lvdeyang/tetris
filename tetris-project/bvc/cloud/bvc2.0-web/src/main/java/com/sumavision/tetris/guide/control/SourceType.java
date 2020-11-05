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
 * <b>日期：</b>2020年9月4日 下午4:38:03
 */
public enum SourceType {
	
	URL("直播流"),
	KNAPSACK_5G("5G背包");
	
	private String name;
	
	private SourceType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static SourceType fromName(String name) throws Exception{
		SourceType[] values = SourceType.values();
		for (SourceType sourceType : values) {
			if(sourceType.getName().equals(name)){
				return sourceType;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
