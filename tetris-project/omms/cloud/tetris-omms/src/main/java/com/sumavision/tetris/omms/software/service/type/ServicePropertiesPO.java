package com.sumavision.tetris.omms.software.service.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务配置属性<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月25日 下午5:36:10
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVICE_PROPERTIES")
public class ServicePropertiesPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 配置id */
	private String propertyKey;
	
	/** 配置名称 */
	private String propertyName;
	
	/** 值类型 */
	private PropertyValueType valueType;
	
	/** 配置默认值 */
	private String propertyDefaultValue;
	
	/** 服务id */
	private Long serviceTypeId;

	@Column(name = "PROPERTY_KEY")
	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	@Column(name = "PROPERTY_NAME")
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VALUE_TYPE")
	public PropertyValueType getValueType() {
		return valueType;
	}

	public void setValueType(PropertyValueType valueType) {
		this.valueType = valueType;
	}

	@Column(name = "PROPERTY_DEFAULT_VALUE")
	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public void setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
	}

	@Column(name = "SERVICE_TYPE_ID")
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	
}
