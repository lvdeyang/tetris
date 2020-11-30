package com.sumavision.tetris.omms.software.service.installation;

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
@Table(name = "TETRIS_OMMS_PROPERTIES")
public class PropertiesPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 配置id */
	private String propertyKey;
	
	/** 配置名称 */
	private String propertyName;
	
	/** 值类型 */
	private PropertyValueType valueType;
	
	/** valueType为ENUM时，存enum项格式：[{name:"", value:""}] */
	private String valueSelect;
	
	/** 配置默认值 */
	private String propertyDefaultValue;
	
	/** 安装包id */
	private Long installationPackageId;
	
	/** 当valueType取值为DBPORT时标识成对的DBIP和DBPORT */
	private String ref;

	@Column(name = "REF")
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

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

	@Column(name = "VALUE_SELECT")
	public String getValueSelect() {
		return valueSelect;
	}

	public void setValueSelect(String valueSelect) {
		this.valueSelect = valueSelect;
	}

	@Column(name = "PROPERTY_DEFAULT_VALUE")
	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public void setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
	}

	@Column(name = "INSTALLATION_PACKAGE_ID")
	public Long getInstallationPackageId() {
		return installationPackageId;
	}

	public void setInstallationPackageId(Long installationPackageId) {
		this.installationPackageId = installationPackageId;
	}
	
}
