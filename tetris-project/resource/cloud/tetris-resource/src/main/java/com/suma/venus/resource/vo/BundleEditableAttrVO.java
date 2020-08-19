package com.suma.venus.resource.vo;

public class BundleEditableAttrVO {

	/**字段名*/
	private String name;
	
	/**字段值*/
	private String value;
	
	/**所属bundleId*/
	private String bundleId;
	
	public BundleEditableAttrVO() {
		super();
	}
	
	public BundleEditableAttrVO(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public BundleEditableAttrVO(String name, String value, String bundleId) {
		super();
		this.name = name;
		this.value = value;
		this.bundleId = bundleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
}
