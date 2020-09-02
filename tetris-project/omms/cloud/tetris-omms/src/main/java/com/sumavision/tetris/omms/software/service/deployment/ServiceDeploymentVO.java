package com.sumavision.tetris.omms.software.service.deployment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServiceDeploymentVO extends AbstractBaseVO<ServiceDeploymentVO, ServiceDeploymentPO>{

	private Long serviceTypeId;
	
	private Long installationPackageId;
	
	private String installFullPath;
	
	private Long serverId;
	
	private JSONObject config;
	
	private String creator;

	private String createTime;
	
	private String step;
	
	private String stepName;
	
	private Integer progress;
	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public ServiceDeploymentVO setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
		return this;
	}

	public Long getInstallationPackageId() {
		return installationPackageId;
	}

	public ServiceDeploymentVO setInstallationPackageId(Long installationPackageId) {
		this.installationPackageId = installationPackageId;
		return this;
	}

	public String getInstallFullPath() {
		return installFullPath;
	}

	public ServiceDeploymentVO setInstallFullPath(String installFullPath) {
		this.installFullPath = installFullPath;
		return this;
	}

	public Long getServerId() {
		return serverId;
	}

	public ServiceDeploymentVO setServerId(Long serverId) {
		this.serverId = serverId;
		return this;
	}

	public JSONObject getConfig() {
		return config;
	}

	public ServiceDeploymentVO setConfig(JSONObject config) {
		this.config = config;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public ServiceDeploymentVO setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public ServiceDeploymentVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getStep() {
		return step;
	}

	public ServiceDeploymentVO setStep(String step) {
		this.step = step;
		return this;
	}

	public String getStepName() {
		return stepName;
	}

	public ServiceDeploymentVO setStepName(String stepName) {
		this.stepName = stepName;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public ServiceDeploymentVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	@Override
	public ServiceDeploymentVO set(ServiceDeploymentPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setServiceTypeId(entity.getServiceTypeId())
			.setInstallationPackageId(entity.getInstallationPackageId())
			.setInstallFullPath(entity.getInstallFullPath())
			.setServerId(entity.getServerId())
			.setConfig(entity.getConfig()==null||"".equals(entity.getConfig())?null:JSON.parseObject(entity.getConfig()))
			.setCreator(entity.getCreator())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setStep(entity.getStep().toString())
			.setStepName(entity.getStep().getName())
			.setProgress(entity.getProgress());
		return this;
	}

}
