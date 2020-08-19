package com.sumavision.tetris.resouce.feign.bundle;

/**
 * bundle参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月9日 下午7:24:15
 */
public class BundleVO {

	private String bundleId;
	
	private String bundleName;
	
	private String DeviceModel;
	
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
		return DeviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		DeviceModel = deviceModel;
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
	
}
