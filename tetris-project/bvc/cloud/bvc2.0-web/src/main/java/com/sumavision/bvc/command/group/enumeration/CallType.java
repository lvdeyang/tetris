package com.sumavision.bvc.command.group.enumeration;

public enum CallType {

	LOCAL_LOCAL("本地呼本地", ""),
	LOCAL_OUTER("本地呼外部", ""),
	OUTER_LOCAL("外部呼本地", "");
	
	private String name;
	
	private String code;
	
	private CallType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

}
