package com.sumavision.tetris.omms.software.service.type;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	
	/** 安装目录 */
	private String installationDirectory;
	
	/** 启动脚本  TODO 需要格式*/
	private String startupScript;
	
	/** 停止脚本  TODO 需要格式 */
	private String shutdownScript;
	
	/** 日志路径 */
	private String logFile;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	@Column(name = "STARTUP_SCRIPT", columnDefinition = "LONGTEXT")
	public String getStartupScript() {
		return startupScript;
	}

	public void setStartupScript(String startupScript) {
		this.startupScript = startupScript;
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
	@Column(name = "LOG_FILE", columnDefinition = "LONGTEXT")
	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	
}
