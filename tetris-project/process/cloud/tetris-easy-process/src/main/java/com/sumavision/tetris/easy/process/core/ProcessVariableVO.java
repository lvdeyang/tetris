package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProcessVariableVO extends AbstractBaseVO<ProcessVariableVO, ProcessVariablePO>{

	private String primaryKey;
	
	private String value;
	
	private String name;
	
	private String dataType;
	
	private String defaultValue;
	
	private String expressionValue;
	
	private boolean removeable;
	
	private String origin;
	
	private String constraintExpression;
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public ProcessVariableVO setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ProcessVariableVO setValue(String value) {
		this.value = value;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProcessVariableVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getDataType() {
		return dataType;
	}

	public ProcessVariableVO setDataType(String dataType) {
		this.dataType = dataType;
		return this;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public ProcessVariableVO setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public String getExpressionValue() {
		return expressionValue;
	}

	public ProcessVariableVO setExpressionValue(String expressionValue) {
		this.expressionValue = expressionValue;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public ProcessVariableVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}
	
	public String getOrigin() {
		return origin;
	}

	public ProcessVariableVO setOrigin(String origin) {
		this.origin = origin;
		return this;
	}

	public String getConstraintExpression() {
		return constraintExpression;
	}

	public ProcessVariableVO setConstraintExpression(String constraintExpression) {
		this.constraintExpression = constraintExpression;
		return this;
	}

	@Override
	public ProcessVariableVO set(ProcessVariablePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPrimaryKey(entity.getPrimaryKey())
			.setName(entity.getName())
			.setDataType(entity.getDataType().getName())
			.setDefaultValue(entity.getDefaultValue())
			.setExpressionValue(entity.getExpressionValue())
			.setRemoveable(entity.getAutoGeneration()==null?true:!entity.getAutoGeneration().booleanValue());
		return this;
	}
	
	public ProcessVariableVO set(ProcessVariablePO entity, boolean removeable) throws Exception {
		this.set(entity)
			.setRemoveable(removeable);
		return this;
	}
	
	public ProcessVariableVO set(ProcessVariablePO entity, String origin) throws Exception {
		this.set(entity)
			.setOrigin(origin);
		return this;
	}
	
	public ProcessVariableVO set(ProcessVariablePO entity, boolean removeable, String origin) throws Exception {
		this.set(entity, origin)
			.setRemoveable(removeable);
		return this;
	}
	
	public ProcessVariableVO set(AccessPointParamPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPrimaryKey(entity.getPrimaryKey())
			.setName(entity.getName())
			.setDefaultValue(entity.getDefaultValue())
			.setRemoveable(false)
			.setConstraintExpression(entity.getConstraintExpression());
		return this;
	}
	
}
