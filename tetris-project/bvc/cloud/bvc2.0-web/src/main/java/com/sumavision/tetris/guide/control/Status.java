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
 * <b>日期：</b>2020年9月7日 下午4:31:40
 */
public enum Status {
	
	START("开始"),
	STOP("停止");
	
	private String name;
	
	private Status(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static Status fromName(String name) throws Exception{
		Status[] values = Status.values();
		for(int i = 0; i < values.length; i++){
			if(values[i].getName().equals(name)){
				return values[i];
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
