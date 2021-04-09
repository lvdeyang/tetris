package com.sumavision.bvc.device.monitor.ptzctrl;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 方向<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午3:31:01
 */
public enum Direction {

	UP("上", "u"),
	DOWN("下", "d"),
	LEFT("左", "l"),
	RIGHT("右", "r");
	
	private String name;
	
	private String protocol;
	
	private Direction(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocol(){
		return this.protocol;
	}
	
	public static Direction fromName(String name) throws Exception{
		Direction[] values = Direction.values();
		for(Direction value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
