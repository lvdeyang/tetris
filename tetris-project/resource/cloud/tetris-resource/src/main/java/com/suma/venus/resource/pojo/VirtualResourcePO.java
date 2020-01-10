package com.suma.venus.resource.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 虚拟资源PO，resourceId相同的一组散列虚拟资源PO组合起来描述一个完整的虚拟资源
 * @author lxw
 *
 */
@Entity
public class VirtualResourcePO extends CommonPO<VirtualResourcePO>{
	
	private String attrName;
	
	private String attrValue;
	
	private String resourceId;
	
	public VirtualResourcePO() {
	}
	
	public VirtualResourcePO(String attrName, String attrValue) {
		this.attrName = attrName;
		this.attrValue = attrValue;
	}

	public VirtualResourcePO(String attrName, String attrValue,String resourceId) {
		this.attrName = attrName;
		this.attrValue = attrValue;
		this.resourceId = resourceId;
	}
	
	@Column(name="attr_name")
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Column(name="attr_value")
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Column(name="resource_id")
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
}
