package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 流程变量来源<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月8日 下午5:16:41
 */
public enum ProcessVariableOrigin {

	CUSTOM("自定义"),
	INTERNAL("内置变量");
	
	private String name;
	
	private ProcessVariableOrigin(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ProcessVariableOrigin fromName(String name) throws Exception{
		ProcessVariableOrigin[] values = ProcessVariableOrigin.values();
		for(ProcessVariableOrigin value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
