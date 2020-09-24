package com.suma.venus.resource.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 附加信息表
 * @author lxw
 *
 */
@Entity
@Table(name = "extra_infopo", uniqueConstraints = {@UniqueConstraint(columnNames={"bundle_id", "name"})})
public class ExtraInfoPO extends CommonPO<ExtraInfoPO>{

	private String name;
	
	private String value;
	
	private String bundleId;

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
