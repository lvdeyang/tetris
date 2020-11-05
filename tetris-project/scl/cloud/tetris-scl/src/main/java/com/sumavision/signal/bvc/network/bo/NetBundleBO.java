package com.sumavision.signal.bvc.network.bo;

import com.suma.venus.resource.pojo.BundlePO;

/**
 * 设备信息--包括net调度信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月28日 上午10:31:44
 */
public class NetBundleBO {

	/** 设备id */
	private String bundleId;
	
	/** 设备ip */
	private String bundleIp;

	/** 网络id */
	private String netId;
	
	/** 网络ip */
	private String netIp;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getBundleIp() {
		return bundleIp;
	}

	public void setBundleIp(String bundleIp) {
		this.bundleIp = bundleIp;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}
	
	public String getNetIp() {
		return netIp;
	}

	public void setNetIp(String netIp) {
		this.netIp = netIp;
	}

	public NetBundleBO toBo(BundlePO entity) throws Exception{
		this.setBundleId(entity.getBundleId());
		this.setBundleIp(entity.getDeviceIp());
		this.setNetId(null);
		this.setNetIp(null);
		return this;
	}
 	
}
