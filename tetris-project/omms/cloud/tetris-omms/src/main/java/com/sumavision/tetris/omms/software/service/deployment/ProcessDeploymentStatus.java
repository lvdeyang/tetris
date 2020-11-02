package com.sumavision.tetris.omms.software.service.deployment;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ProcessDeploymentStatus {
	
	ONLINE("在线"),
	OFFLINE("离线");
	
	private String name;
	
	public String getName(){
		return this.name;
	}
	
	private ProcessDeploymentStatus(String name){
		this.name = name;
	}
	
	public static ProcessDeploymentStatus fromName(String name) throws Exception{
		ProcessDeploymentStatus[] values = ProcessDeploymentStatus.values();
		for (ProcessDeploymentStatus value : values) {
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
