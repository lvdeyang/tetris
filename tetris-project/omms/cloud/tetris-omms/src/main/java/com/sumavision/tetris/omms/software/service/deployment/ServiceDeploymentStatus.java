package com.sumavision.tetris.omms.software.service.deployment;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ServiceDeploymentStatus {

	INSTALLED("已安装"),
	UNINSTALLED("已卸载");
	
	private String name;
	
	private ServiceDeploymentStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ServiceDeploymentStatus fromName(String name) throws Exception{
		ServiceDeploymentStatus[] values = ServiceDeploymentStatus.values();
		for(ServiceDeploymentStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
