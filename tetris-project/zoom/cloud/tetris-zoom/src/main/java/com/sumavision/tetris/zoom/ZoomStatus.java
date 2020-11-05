package com.sumavision.tetris.zoom;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 会议状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午9:11:51
 */
public enum ZoomStatus {

	START("开始"),
	STOP("停止");
	
	private String name;
	
	private ZoomStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ZoomStatus fromName(String name) throws Exception{
		ZoomStatus[] values = ZoomStatus.values();
		for(ZoomStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
