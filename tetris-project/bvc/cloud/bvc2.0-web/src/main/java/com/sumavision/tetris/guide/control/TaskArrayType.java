package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TaskArrayType {

	PASSBY("既不转封装也不转码，不下发task_array","PASSBY"),
	STREAM("只转封装,不下发task_array","STREAM"),
	TRANSCODE("转码","TRANSCODE");
	
	private String name;
	
	private String code;
	
	private TaskArrayType(String name,String code){
		this.name=name;
		this.code=code;
	}

	public String getName() {
		return name;
	}

	public TaskArrayType forName(String name) throws ErrorTypeException{
		TaskArrayType[] values=TaskArrayType.values();
		for(TaskArrayType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
