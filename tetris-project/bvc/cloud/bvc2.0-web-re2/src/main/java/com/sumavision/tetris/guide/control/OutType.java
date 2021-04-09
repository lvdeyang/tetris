/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 输出类型：预监任务，切换任务<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月30日 上午9:03:32
 */
public enum OutType {
	MONITOR("monitor"),
	SWITCH("switch");
	
	private String name;
	
	private OutType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static OutType fromName(String name) throws Exception{
		OutType[] values = OutType.values();
		for (OutType outType : values) {
			if(outType.getName().equals(name)){
				return outType;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
