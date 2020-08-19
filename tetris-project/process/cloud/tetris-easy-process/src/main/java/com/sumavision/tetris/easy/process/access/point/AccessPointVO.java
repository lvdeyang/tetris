package com.sumavision.tetris.easy.process.access.point;

import java.util.Set;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AccessPointVO extends AbstractBaseVO<AccessPointVO, AccessPointPO>{

	private String name;
	
	private String type;
	
	private String method;
	
	private Long serviceId;
	
	private String serviceType;
	
	private String serviceName;
	
	private String remarks;
	
	private Set<AccessPointParamVO> params;
	
	private Set<JointConstraintExpressionVO> constraints;
	
	public String getName() {
		return name;
	}

	public AccessPointVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public AccessPointVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public AccessPointVO setMethod(String method) {
		this.method = method;
		return this;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public AccessPointVO setServiceId(Long serviceId) {
		this.serviceId = serviceId;
		return this;
	}

	public String getServiceType() {
		return serviceType;
	}

	public AccessPointVO setServiceType(String serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	public String getServiceName() {
		return serviceName;
	}

	public AccessPointVO setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public AccessPointVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public Set<AccessPointParamVO> getParams() {
		return params;
	}

	public AccessPointVO setParams(Set<AccessPointParamVO> params) {
		this.params = params;
		return this;
	}
	
	public Set<JointConstraintExpressionVO> getConstraints() {
		return constraints;
	}

	public AccessPointVO setConstraints(Set<JointConstraintExpressionVO> constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public AccessPointVO set(AccessPointPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().getName())
			.setMethod(entity.getMethod())
			.setServiceId(entity.getServiceId())
			.setRemarks(entity.getRemarks());
		return this;
	}
	
	public AccessPointVO set(AccessPointPO entity, ServiceType serviceType, String serviceName) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().getName())
			.setMethod(entity.getMethod())
			.setServiceId(entity.getServiceId())
			.setServiceType(serviceType.getName())
			.setServiceName(serviceName)
			.setRemarks(entity.getRemarks());
		return this;
	}

}
