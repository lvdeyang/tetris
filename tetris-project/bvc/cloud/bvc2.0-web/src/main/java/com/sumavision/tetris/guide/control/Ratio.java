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
 * <b>日期：</b>2020年9月16日 下午1:21:49
 */
public enum Ratio {
	_16_10("16:10"),
	_16_9("16:9"),
	_5_4("5:4"),
	_4_3("4:3");
	
	private String name;
	
	private Ratio(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static Ratio fromName(String name) throws Exception{
		Ratio[] values = Ratio.values();
		for (Ratio ratio : values) {
			if(ratio.getName().equals(name)){
				return ratio;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
