package com.sumavision.tetris.omms.software.service.deployment;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum DeploymentStep {

	UPLOAD("上传"),
	CONFIG("配置"),
	INSTALL("安装"),
	COMPLETE("完成");
	
	private String name;
	
	private DeploymentStep(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static DeploymentStep fromName(String name) throws Exception{
		DeploymentStep[] values = DeploymentStep.values();
		for(DeploymentStep value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
