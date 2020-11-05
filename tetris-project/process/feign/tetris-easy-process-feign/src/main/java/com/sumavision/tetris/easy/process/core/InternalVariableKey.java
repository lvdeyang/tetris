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
	START_USER_NICKNAME("__startUserNickname__", "启动用户昵称"),
	START_TIME("__startTime__", "启动时间"),
	ACCESS_POINT_ID("__accessPointId__", "接入点id"),
	VARIABLE_CONTEXT("__variableContext__", "流程上下文变量"),
	REQUEST_HEADERS("__requestHeaders__", "http请求头"),
	NODE_HISTORY("__nodeHistory__", "节点历史记录"),
	NODE_YTPE_USER("__user__", "用户节点类型"),
	NODE_TYPE_SERVICE("__service__", "服务节点类型");
	
	private String variableKey;
	
	private String name;
	
	private InternalVariableKey(String variableKey, String name){
		this.variableKey = variableKey;
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
