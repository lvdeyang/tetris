package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 模板可编辑字段PO
 * @author lxw
 *
 */
@Entity
public class TemplateEditableAttrPO extends CommonPO<TemplateEditableAttrPO>{     

	/**所属模板名称*/
	private String deviceModel;
	
	/**字段名*/
	private String name;
	
	/**字段默认值，可为空*/
	private String defaultValue;
	
	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
