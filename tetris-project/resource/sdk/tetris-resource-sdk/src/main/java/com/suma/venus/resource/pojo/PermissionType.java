package com.suma.venus.resource.pojo;

public enum PermissionType {

	VOD("点播", "hasWritePrivilege"),
	RECORD_LOCAL("本地录制", "hasLocalReadPrivilege"),
	RECORD_SERVER("服务端录制", "hasReadPrivilege"),
	DOWNLOAD("下载", "hasDownloadPrivilege"),
	PTZ("云台", "hasCloudPrivilege");
	
	private String name;
	
	private String referencePrivilegeKey;
	
	private PermissionType(String name, String referencePrivilegeKey){
		this.name = name;
		this.referencePrivilegeKey = referencePrivilegeKey;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getReferencePrivilegeKey(){
		return this.referencePrivilegeKey;
	}
	
}
