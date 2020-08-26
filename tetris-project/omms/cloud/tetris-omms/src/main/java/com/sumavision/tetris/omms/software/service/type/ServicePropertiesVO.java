package com.sumavision.tetris.omms.software.service.type;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServicePropertiesVO extends AbstractBaseVO<ServicePropertiesVO, ServicePropertiesPO>{

	/** 配置id */
	private String propertyKey;
	
	/** 配置名称 */
	private String propertyName;
	
	/** 值类型 */
	private String valueType;
	
	/** 值类型名称 */
	private String valueTypeName;
	
	/** 配置默认值 */
	private String propertyDefaultValue;
	
	public String getPropertyKey() {
		return propertyKey;
	}

	public ServicePropertiesVO setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
		return this;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public ServicePropertiesVO setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public String getValueType() {
		return valueType;
	}

	public ServicePropertiesVO setValueType(String valueType) {
		this.valueType = valueType;
		return this;
	}

	public String getValueTypeName() {
		return valueTypeName;
	}

	public ServicePropertiesVO setValueTypeName(String valueTypeName) {
		this.valueTypeName = valueTypeName;
		return this;
	}

	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public ServicePropertiesVO setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
		return this;
	}

	@Override
	public ServicePropertiesVO set(ServicePropertiesPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPropertyKey(entity.getPropertyKey())
			.setPropertyName(entity.getPropertyName())
			.setPropertyDefaultValue(entity.getPropertyDefaultValue())
			.setValueType(entity.getValueType().toString())
			.setValueTypeName(entity.getValueType().getName());
		return this;
	}

}
