/**
 * 
 */
package com.sumavision.tetris.omms.software.service.installation;


import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;


public class BackupInformationVO extends AbstractBaseVO<BackupInformationVO, BackupInformationPO>{

	/** 服务器id */
	private Long serverId;
	
	/** 服务器名称 */
	private String serverName;
	
	/** 服务器ip */
	private String serverIp;

	/** 服务部署id */
	private Long deploymentId;
	
	/** 服务名称 */
	private String deploymentName;
	
	/** 安装包id */
	private Long installPackageId;
	
	/** 安装包名称 */
	private String fileName;
	
	/** 安装包版本号 */
	private String version;
	
	/** 备份注释信息 */
	private String notes;
	
	/** 备份时间 */
	private String backupTime;
	
	/** 备份全路径 */
	private String backupFullPath;
	
	/** 配置信息 */
	private String config;

	public Long getServerId() {
		return serverId;
	}

	public BackupInformationVO setServerId(Long serverId) {
		this.serverId = serverId;
		return this;
	}
	
	public String getServerName() {
		return serverName;
	}

	public BackupInformationVO setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}

	public String getServerIp() {
		return serverIp;
	}

	public BackupInformationVO setServerIp(String serverIp) {
		this.serverIp = serverIp;
		return this;
	}

	public Long getDeploymentId() {
		return deploymentId;
	}

	public BackupInformationVO setDeploymentId(Long deploymentId) {
		this.deploymentId = deploymentId;
		return this;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public BackupInformationVO setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
		return this;
	}

	public Long getInstallPackageId() {
		return installPackageId;
	}

	public BackupInformationVO setInstallPackageId(Long installPackageId) {
		this.installPackageId = installPackageId;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public BackupInformationVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public BackupInformationVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getNotes() {
		return notes;
	}

	public BackupInformationVO setNotes(String notes) {
		this.notes = notes;
		return this;
	}

	public String getBackupTime() {
		return backupTime;
	}

	public BackupInformationVO setBackupTime(String backupTime) {
		this.backupTime = backupTime;
		return this;
	}

	public String getBackupFullPath() {
		return backupFullPath;
	}

	public BackupInformationVO setBackupFullPath(String backupFullPath) {
		this.backupFullPath = backupFullPath;
		return this;
	}

	public String getConfig() {
		return config;
	}

	public BackupInformationVO setConfig(String config) {
		this.config = config;
		return this;
	}

	@Override
	public BackupInformationVO set(BackupInformationPO entity) throws Exception {
		this.setId(entity.getId())
			.setServerId(entity.getServerId())
			.setServerName(entity.getServerName())
			.setServerIp(entity.getServerIp())
			.setDeploymentId(entity.getDeploymentId())
			.setDeploymentName(entity.getDeploymentName())
			.setInstallPackageId(entity.getInstallPackageId())
			.setFileName(entity.getFileName())
			.setVersion(entity.getVersion())
			.setNotes(entity.getNotes())
			.setBackupTime(entity.getBackupTime() == null?"":DateUtil.format(entity.getBackupTime(), DateUtil.dateTimePattern))
			.setBackupFullPath(entity.getBackupFullPath())
			.setConfig(entity.getConfig());
		return this;
	}
}
