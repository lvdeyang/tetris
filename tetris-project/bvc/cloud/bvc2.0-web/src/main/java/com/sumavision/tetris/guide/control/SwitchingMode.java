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
 * <b>日期：</b>2020年9月23日 下午3:52:43
 */
public enum SwitchingMode {
	
	TRANSCODE("转码"),
	FRAME("按帧切换"),
	DIRECTOR("直接切换");
	
	private String name;
	
//	private String code;
	
	private SwitchingMode(String name){
		this.name = name;
//		this.code=code;
	}
	
	public String getName(){
		return name;
	}
	
//	public String getCode() {
//		return code;
//	}

	public static SwitchingMode fromName(String name) throws Exception{
		SwitchingMode[] value = SwitchingMode.values();
		for (SwitchingMode switchingMode : value) {
			if(switchingMode.getName().equals(name)){
				return switchingMode;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
