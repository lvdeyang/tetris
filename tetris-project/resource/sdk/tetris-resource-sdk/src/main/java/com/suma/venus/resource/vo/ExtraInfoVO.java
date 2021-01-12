package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.ExtraInfoPO;

public class ExtraInfoVO {

	private String name;
	
	private String value;
	
	private String bundleId;
	
	private String worknodeId;

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

	public String getWorknodeId() {
		return worknodeId;
	}

	public void setWorknodeId(String worknodeId) {
		this.worknodeId = worknodeId;
	}
	
	public ExtraInfoPO toPO(){
		ExtraInfoPO po = new ExtraInfoPO();
		po.setName(this.getName());
		po.setValue(this.getValue());
		po.setBundleId(this.getBundleId());
		return po;
	}
	
	public static ExtraInfoVO fromPO(ExtraInfoPO po){
		ExtraInfoVO vo = new ExtraInfoVO();
		vo.setName(po.getName());
		vo.setValue(po.getValue());
		vo.setBundleId(po.getBundleId());
		return vo;
	}
}
