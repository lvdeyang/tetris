package com.sumavision.tetris.omms.software.service.deployment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务部署情况<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 上午11:14:28
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVICE_DEPLOYMENT")
public class ServiceDeploymentPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务类型id */
	private Long serviceTypeId;
	
	/** 安装目录 */
	private String installationDirectory;
	
	/** 启动脚本 */
	private String startupScript;
	
	/** 停止脚本 */
	private String shutdownScript;
	
	/** 日志路径 */
	private String logFile;
	
	/** 服务器id */
	private Long serverId;
	
	/** 参数列表 TODO 格式 */
	private String params;
	
	/** 创建者 */
	private String creator;

	/** 创建时间 */
	private Date createTime;

	@Column(name = "SERVICE_TYPE_ID")
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
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

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Lob
	@Column(name = "PARAMS", columnDefinition = "LONGTEXT")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
