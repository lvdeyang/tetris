package com.suma.venus.resource.controller.feign;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class YjgbVO extends AbstractBaseVO<YjgbVO, BundlePO>{

	private String bundleId;
	
	private String bundleName;
	
	private String DeviceModel;
	
	private String streamUrl;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getDeviceModel() {
		return DeviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		DeviceModel = deviceModel;
	}

	@Override
	public YjgbVO set(BundlePO entity) throws Exception {
		this.setId(entity.getId());
		this.setBundleId(entity.getBundleId());
		this.setBundleName(entity.getBundleName());
		this.setStreamUrl(entity.getStreamUrl());
		this.setDeviceModel(entity.getDeviceModel());
		return this;
	}
	
}
