package com.sumavision.bvc.device.monitor.live;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 点播设备类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 下午3:22:13
 */
public enum LiveType {

	XT_LOCAL("xt点播本地"),
	XT_XT("xt点播xt"),
	LOCAL_XT("本地点播xt"),
	LOCAL_LOCAL("本地点播本地");
	
	private String name;
	
	private LiveType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LiveType fromName(String name) throws Exception{
		LiveType[] values = LiveType.values();
		for(LiveType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
