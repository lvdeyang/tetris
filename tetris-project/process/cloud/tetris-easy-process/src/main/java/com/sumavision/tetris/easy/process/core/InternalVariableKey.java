package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 内置流程变量primaryKey<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月9日 上午10:47:03
 */
public enum InternalVariableKey {

	PROCESS_INSTANCE_ID("__processInstanceId__", "流程实例id"),
	START_USER_ID("__startUserId__", "启动用户id"),
	ACCESS_POINT_ID("__accessPointId__", "接入点id");
	
	private String variableKey;
	
	private String name;
	
	private InternalVariableKey(String variableKey, String name){
		this.name = name;
	}
	
	public String getVariableKey(){
		return this.variableKey;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static InternalVariableKey fromName(String name) throws Exception{
		InternalVariableKey[] values = InternalVariableKey.values();
		for(InternalVariableKey value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static InternalVariableKey fromVariableKey(String variableKey) throws Exception{
		InternalVariableKey[] values = InternalVariableKey.values();
		for(InternalVariableKey value:values){
			if(value.getVariableKey().equals(variableKey)){
				return value;
			}
		}
		throw new ErrorTypeException("variableKey", variableKey);
	}
	
}
