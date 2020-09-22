package com.sumavision.bvc.api.controller;

import com.suma.venus.resource.pojo.BundlePO;

/**
 * 用于应急广播查询的bundle参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月9日 下午7:24:15
 */
public class EmergentBundleVO {

	private String bundleId;
	
	private String bundleName;
	
	private String deviceModel;
	
	private String streamUrl;
	
	private String identify;

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

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
	
	public EmergentBundleVO set(BundlePO bundle){
		this.bundleId = bundle.getBundleId();
		this.bundleName = bundle.getBundleName();
		this.deviceModel = bundle.getDeviceModel();
		this.identify = bundle.getIdentify();
		this.streamUrl = bundle.getStreamUrl();
		return this;
	}
	
}
