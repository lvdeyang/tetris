package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * bundle可编辑字段PO，用户针对模板中的可编辑字段配置而生成
 * @author lxw
 *
 */
@Entity
public class BundleEditableAttrPO extends CommonPO<BundleEditableAttrPO>{

	/**字段名*/
	private String name;
	
	/**字段值*/
	private String value;
	
	/**所属bundleId*/
	private String bundleId;

	public BundleEditableAttrPO() {
		super();
	}
	
	public BundleEditableAttrPO(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public BundleEditableAttrPO(String name, String value, String bundleId) {
		super();
		this.name = name;
		this.value = value;
		this.bundleId = bundleId;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
}
