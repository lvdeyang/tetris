package com.sumavision.tetris.omms.software.service.deployment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	/** 安装包id */
	private Long installationPackageId;
	
	/** 安装全路径 */
	private String installFullPath;
	
	/** 服务器id */
	private Long serverId;
	
	/** 参数列表config.ini {key:value, key1:value1}*/
	private String config;
	
	/** 创建者 */
	private String creator;

	/** 创建时间 */
	private Date createTime;
	
	/** 部署阶段 */
	private DeploymentStep step;
	
	/** 阶段进度 */
	private Integer progress;
	
	/** 是否发生异常 */
	private Boolean error;
	
	/** 异常信息 */
	private String errorMessage;
	
	/** 服务状态 */
	private ServiceDeploymentStatus status;
	
	@Column(name = "SERVICE_TYPE_ID")
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	@Column(name = "INSTALLATION_PACKAGE_ID")
	public Long getInstallationPackageId() {
		return installationPackageId;
	}

	public void setInstallationPackageId(Long installationPackageId) {
		this.installationPackageId = installationPackageId;
	}

	@Column(name = "INSTALL_FULL_PATH")
	public String getInstallFullPath() {
		return installFullPath;
	}

	public void setInstallFullPath(String installFullPath) {
		this.installFullPath = installFullPath;
	}

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Lob
	@Column(name = "CONFIG", columnDefinition="TEXT")
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STEP")
	public DeploymentStep getStep() {
		return step;
	}

	public void setStep(DeploymentStep step) {
		this.step = step;
	}

	@Column(name = "PROGRESS")
	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	@Column(name = "ERROR")
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	@Column(name = "ERROR_MESSAGE")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public ServiceDeploymentStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceDeploymentStatus status) {
		this.status = status;
	}
}
