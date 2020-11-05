package com.sumavision.tetris.zoom;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 会议模式<br/>
 * <b>作者:</b>吕德阳<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午9:11:30
 */
public enum ZoomMode {

	DISCUSSION_MODE("讨论模式"),
	CONVERSION_MODE("大会模式");
	
	private String name;
	
	private ZoomMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ZoomMode fromName(String name) throws Exception{
		ZoomMode[] values = ZoomMode.values();
		for(ZoomMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
