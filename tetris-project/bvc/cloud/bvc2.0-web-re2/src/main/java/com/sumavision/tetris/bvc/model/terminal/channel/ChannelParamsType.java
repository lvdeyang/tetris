package com.sumavision.tetris.bvc.model.terminal.channel;

import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 通道分辨率自适应参数类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午3:14:40
 */
public enum ChannelParamsType {

	UHD("超高清", 4, GearsLevel.LEVEL_4),
	HD("高清", 3, GearsLevel.LEVEL_3),
	STANDARD("标清", 2, GearsLevel.LEVEL_2),
	GENERAL("普通", 1, GearsLevel.LEVEL_1),
	/** 这个就是没有自适应参数 */
	DEFAULT("默认", -1, null);
	
	private String name;
	
	/** 级别 */
	private int level;
	
	private GearsLevel gearsLevel;
	
	private ChannelParamsType(String name, int level, GearsLevel gearsLevel){
		this.name = name;
		this.level = level;
		this.gearsLevel = gearsLevel;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getLevel(){
		return this.level;
	}

	public GearsLevel getGearsLevel() {
		return gearsLevel;
	}
	
	public static ChannelParamsType fromName(String name) throws Exception{
		ChannelParamsType[] values = ChannelParamsType.values();
		for(ChannelParamsType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
