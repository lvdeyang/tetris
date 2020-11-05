package com.sumavision.tetris.omms.software.service.type;

public enum ServiceType {

	EUREKA("微服务中心", 
			"/home/install", 
			"installScript", 
			"installScriptPath", 
			"startupScript", 
			"startupScriptPath", 
			"shutdownScript", 
			"shutdownScriptPath", 
			"logFile", 
			GroupType.JAVA_SERVER),
	
	ZUUL("微服务网关", 
		  "/home/install", 
		  "installScript", 
		  "installScriptPath", 
		  "startupScript", 
		  "startupScriptPath", 
		  "shutdownScript", 
		  "shutdownScriptPath", 
		  "logFile", 
		  GroupType.JAVA_SERVER),
	
	FILE_TRANSCODING("文件转码", 
		  "/home/install", 
		  "installScript", 
		  "installScriptPath", 
		  "startupScript", 
		  "startupScriptPath", 
		  "shutdownScript", 
		  "shutdownScriptPath", 
		  "logFile", 
		  GroupType.C_SERVER);
	
	private String name;
	
	private String installationDirectory;
	
	private String installScript;
	
	private String installScriptPath;
	
	private String startupScript;
	
	private String startupScriptPath;
	
	private String shutdownScript;
	
	private String shutdownScriptPath;
	
	private String logFile;
	
	private GroupType groupType;

	private ServiceType(
			String name,
			String installationDirectory,
			String installScript,
			String installScriptPath,
			String startupScript,
			String startupScriptPath,
			String shutdownScript,
			String shutdownScriptPath,
			String logFile,
			GroupType groupType){
		this.name = name;
		this.installationDirectory = installationDirectory;
		this.installScript = installScript;
		this.installScriptPath = installScriptPath;
		this.startupScript = startupScript;
		this.startupScriptPath = startupScriptPath;
		this.shutdownScript = shutdownScript;
		this.shutdownScriptPath = shutdownScriptPath;
		this.logFile = logFile;
		this.groupType = groupType;
	}
	
	public String getName(){
		return name;
	}

	public String getInstallationDirectory(){
		return installationDirectory;
	}

	public String getInstallScript(){
		return installScript;
	}

	public String getInstallScriptPath(){
		return installScriptPath;
	}

	public String getStartupScript(){
		return startupScript;
	}

	public String getStartupScriptPath(){
		return startupScriptPath;
	}

	public String getShutdownScript(){
		return shutdownScript;
	}

	public String getShutdownScriptPath(){
		return shutdownScriptPath;
	}

	public String getLogFile(){
		return logFile;
	}

	public GroupType getGroupType(){
		return groupType;
	}
	
}
