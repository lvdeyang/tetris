package com.sumavision.tetris.omms.software.service.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 上午11:23:22
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVICE_TYPE")
public class ServiceTypePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务类型名称 */
	private String name;
	
	/** 服务类型枚举 */
	private ServiceType serviceType;
	
	/** 安装包目录 */
	private String installationDirectory;
	
	/** 安装脚本*/
	private String installScript;
	
	/** 安装脚本路径-服务器路径 */
	private String installScriptPath;
	
	/** 启动脚本*/
	private String startupScript;
	
	/** 启动脚本路径-服务器路径  */
	private String startupScriptPath;
	
	/** 停止脚本*/
	private String shutdownScript;
	
	/** 停止脚本路径-服务器路径  */
	private String shutdownScriptPath;
	
	/** 日志路径 */
	private String logFile;
	
	/** 服务类型分组类型枚举 */
	private GroupType groupType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SERVICE_TYPE")
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Lob
	@Column(name = "INSTALLATION_DIRECTORY", columnDefinition = "LONGTEXT")
	public String getInstallationDirectory() {
		return installationDirectory;
	}

	public void setInstallationDirectory(String installationDirectory) {
		this.installationDirectory = installationDirectory;
	}

	@Lob
	@Column(name = "INSTALL_SCRIPT", columnDefinition = "LONGTEXT")
	public String getInstallScript() {
		return installScript;
	}

	public void setInstallScript(String installScript) {
		this.installScript = installScript;
	}

	@Lob
	@Column(name = "INSTALL_SCRIPT_PATH", columnDefinition = "LONGTEXT")
	public String getInstallScriptPath() {
		return installScriptPath;
	}

	public void setInstallScriptPath(String installScriptPath) {
		this.installScriptPath = installScriptPath;
	}

	@Lob
	@Column(name = "STARTUP_SCRIPT", columnDefinition = "LONGTEXT")
	public String getStartupScript() {
		return startupScript;
	}

	public void setStartupScript(String startupScript) {
		this.startupScript = startupScript;
	}

	@Lob
	@Column(name = "STARTUP_SCRIPT_PATH", columnDefinition = "LONGTEXT")
	public String getStartupScriptPath() {
		return startupScriptPath;
	}

	public void setStartupScriptPath(String startupScriptPath) {
		this.startupScriptPath = startupScriptPath;
	}

	@Lob
	@Column(name = "SHUTDOWN_SCRIPT", columnDefinition = "LONGTEXT")
	public String getShutdownScript() {
		return shutdownScript;
	}

	public void setShutdownScript(String shutdownScript) {
		this.shutdownScript = shutdownScript;
	}

	@Lob
	@Column(name = "SHUTDOWN_SCRIPT_PATH", columnDefinition = "LONGTEXT")
	public String getShutdownScriptPath() {
		return shutdownScriptPath;
	}

	public void setShutdownScriptPath(String shutdownScriptPath) {
		this.shutdownScriptPath = shutdownScriptPath;
	}

	@Lob
	@Column(name = "LOG_FILE", columnDefinition = "LONGTEXT")
	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "GROUP_TYPE")
	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}
	
}
