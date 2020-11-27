package com.sumavision.bvc.control.device.group.contacts;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupContactsVO extends AbstractBaseVO<DeviceGroupContactsVO, DeviceGroupContactsPO>{

	/** 联系人id*/
	private Long userId;
	
	/** 设备id*/
	private String bundleId;

	public Long getUserId() {
		return userId;
	}

	public DeviceGroupContactsVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupContactsVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}
	
	@Override
	public DeviceGroupContactsVO set(DeviceGroupContactsPO entity) throws Exception {
		
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUserId(entity.getUserId())
			.setBundleId(entity.getBundleId()==null?"":entity.getBundleId());
		
		return this;
	}

}
