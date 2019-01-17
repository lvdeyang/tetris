package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AccessPointParamVO extends AbstractBaseVO<AccessPointParamVO, AccessPointParamPO>{

	private String name;
	
	private String primaryKey;
	
	private String defaultValue;
	
	private String type;
	
	private String direction;
	
	private String constraintExpression;
	
	private Integer serial;
	
	private Long accessPointId;
	
	private String accessPointName;
	
	private String remarks;
	
	public String getName() {
		return name;
	}

	public AccessPointParamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public AccessPointParamVO setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public AccessPointParamVO setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public String getType() {
		return type;
	}

	public AccessPointParamVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public AccessPointParamVO setDirection(String direction) {
		this.direction = direction;
		return this;
	}

	public String getConstraintExpression() {
		return constraintExpression;
	}

	public AccessPointParamVO setConstraintExpression(String constraintExpression) {
		this.constraintExpression = constraintExpression;
		return this;
	}

	public Integer getSerial() {
		return serial;
	}

	public AccessPointParamVO setSerial(Integer serial) {
		this.serial = serial;
		return this;
	}

	public Long getAccessPointId() {
		return accessPointId;
	}

	public AccessPointParamVO setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
		return this;
	}

	public String getAccessPointName() {
		return accessPointName;
	}

	public AccessPointParamVO setAccessPointName(String accessPointName) {
		this.accessPointName = accessPointName;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public AccessPointParamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	@Override
	public AccessPointParamVO set(AccessPointParamPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setPrimaryKey(entity.getPrimaryKey())
			.setDefaultValue(entity.getDefaultValue())
			.setType(entity.getType().getName())
			.setDirection(entity.getDirection().getName())
			.setConstraintExpression(entity.getConstraintExpression())
			.setSerial(entity.getSerial())
			.setAccessPointId(entity.getAccessPointId())
			.setRemarks(entity.getRemarks());
		return this;
	}
	
	public AccessPointParamVO set(AccessPointParamPO entity, String accessPointName) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setPrimaryKey(entity.getPrimaryKey())
			.setDefaultValue(entity.getDefaultValue())
			.setType(entity.getType().getName())
			.setDirection(entity.getDirection().getName())
			.setConstraintExpression(entity.getConstraintExpression())
			.setSerial(entity.getSerial())
			.setAccessPointId(entity.getAccessPointId())
			.setAccessPointName(accessPointName)
			.setRemarks(entity.getRemarks());
		return this;
	}

}
