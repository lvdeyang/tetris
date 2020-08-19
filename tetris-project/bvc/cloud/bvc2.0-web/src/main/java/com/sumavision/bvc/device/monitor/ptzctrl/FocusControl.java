package com.sumavision.bvc.device.monitor.ptzctrl;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 焦距控制<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午3:50:19
 */
public enum FocusControl {

	NEAR("近", "near"),
	FAR("远", "far");
	
	private String name;
	
	private String protocol;
	
	private FocusControl(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocol(){
		return this.protocol;
	}
	
	public static FocusControl fromName(String name) throws Exception{
		FocusControl[] values = FocusControl.values();
		for(FocusControl value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
