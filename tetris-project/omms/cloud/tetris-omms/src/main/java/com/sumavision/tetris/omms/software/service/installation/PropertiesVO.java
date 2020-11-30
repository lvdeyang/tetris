package com.sumavision.tetris.omms.software.service.installation;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class PropertiesVO extends AbstractBaseVO<PropertiesVO, PropertiesPO>{

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
	
	/** valueType为ENUM时，存enum项格式：[{name:"", value:""}] */
	private String valueSelect;
	
	/** 配置属性值 */
	private String propertyValue;
	
	/***/
	private String ref;
	
	public String getValueSelect() {
		return valueSelect;
	}

	public PropertiesVO setValueSelect(String valueSelect) {
		this.valueSelect = valueSelect;
		return this;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public PropertiesVO setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
		return this;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public PropertiesVO setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public String getValueType() {
		return valueType;
	}

	public PropertiesVO setValueType(String valueType) {
		this.valueType = valueType;
		return this;
	}

	public String getValueTypeName() {
		return valueTypeName;
	}

	public PropertiesVO setValueTypeName(String valueTypeName) {
		this.valueTypeName = valueTypeName;
		return this;
	}

	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public PropertiesVO setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
		return this;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public PropertiesVO setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
		return this;
	}
	
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Override
	public PropertiesVO set(PropertiesPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPropertyKey(entity.getPropertyKey())
			.setPropertyName(entity.getPropertyName())
			.setPropertyDefaultValue(entity.getPropertyDefaultValue())
			.setValueType(entity.getValueType().toString())
			.setValueTypeName(entity.getValueType().getName())
			.setValueSelect(entity.getValueSelect())
		    .setRef(entity.getRef()==null?"":entity.getRef());
		return this;
	}

}
