package com.sumavision.bvc.control.device.group.contacts;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="DEVICE_GROUP_CONTACTS")
public class DeviceGroupContactsPO extends AbstractBasePO{
	
	/** */
	private static final long serialVersionUID = 1L;

	/** 联系人id*/
	private Long userId;
	
	/** 设备id*/
	private String bundleId;

	public Long getUserId() {
		return userId;
	}

	public DeviceGroupContactsPO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupContactsPO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}
	
}
