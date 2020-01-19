package com.sumavision.signal.bvc.resource.dto;

import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;

public class BundleOnlineStatusDTO {

	/****************************
	 * 以下字段来自于ChannelSchemePO
	 ****************************/
	
	private String bundleId;
	
	private ONLINE_STATUS onlineStatus;
	
	public BundleOnlineStatusDTO(
			String bundleId,
			ONLINE_STATUS onlineStatus
			){
		this.bundleId = bundleId;
		this.setOnlineStatus(onlineStatus);
	}

	public String getBundleId() {
		return bundleId;
	}

	public BundleOnlineStatusDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public ONLINE_STATUS getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(ONLINE_STATUS onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
		
}
