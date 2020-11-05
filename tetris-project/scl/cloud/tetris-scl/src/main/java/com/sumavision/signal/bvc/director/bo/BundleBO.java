package com.sumavision.signal.bvc.director.bo;

/**
 * 设备<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午2:45:24
 */
public class BundleBO {

	private String bundleId;
	
	private String channelId;
	
	private String deviceModel;
	
	private String deviceIp;
	
	private String index;
	
	private CodeParamBO codeParam;
	
	private String passBy;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public CodeParamBO getCodeParam() {
		return codeParam;
	}

	public void setCodeParam(CodeParamBO codeParam) {
		this.codeParam = codeParam;
	}

	public String getPassBy() {
		return passBy;
	}

	public void setPassBy(String passBy) {
		this.passBy = passBy;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
}
