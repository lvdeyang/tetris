/**
 * 
 */
package com.sumavision.tetris.omms.software.service.installation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 安装包备份信息<br/>
 * <b>作者:</b>jiajun<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月15日 下午5:29:50
 */
@Entity
@Table(name = "TETRIS_OMMS_BACKUP_INFORMATION")
public class BackupInformationPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
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
	private Date backupTime;
	
	/** 备份全路径 */
	private String backupFullPath;
	
	/** 配置信息 */
	private String config;

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Column(name = "SERVER_NAME")
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name = "SERVER_IP")
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name = "DEPLOYMENT_ID")
	public Long getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(Long deploymentId) {
		this.deploymentId = deploymentId;
	}

	@Column(name = "DEPLOYMENT_NAME")
	public String getDeploymentName() {
		return deploymentName;
	}

	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}

	@Column(name = "INSTALLPACKAGE_ID")
	public Long getInstallPackageId() {
		return installPackageId;
	}

	public void setInstallPackageId(Long installPackageId) {
		this.installPackageId = installPackageId;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BACKUP_TIME")
	public Date getBackupTime() {
		return backupTime;
	}

	public void setBackupTime(Date backupTime) {
		this.backupTime = backupTime;
	}

	@Column(name = "BACKUP_FULL_PATH")
	public String getBackupFullPath() {
		return backupFullPath;
	}

	public void setBackupFullPath(String backupFullPath) {
		this.backupFullPath = backupFullPath;
	}

	@Lob
	@Column(name = "CONFIG", columnDefinition="TEXT")
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
}
