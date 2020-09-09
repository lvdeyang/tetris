package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 播放器资源参数
 * @author lxw
 *
 */
public class PlayerBundleBO {

	private String bundleId;
	
	private String bundleName;
	
	private List<String> channelIds;
	
	/**无用*/
	private String bundleNum;
	
	/**播放器号码*/
	private String username;
	
	private String password;
	
	/** 设备类型，对应BundlePO的bundleType，如VenusTerminal */
	private String bundleType;

	private String accessLayerId;
	
	private String accessLayerIp;
	
	private Integer accessLayerPort;
	
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public List<String> getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(List<String> channelIds) {
		this.channelIds = channelIds;
	}

	public String getBundleNum() {
		return bundleNum;
	}

	public void setBundleNum(String bundleNum) {
		this.bundleNum = bundleNum;
	}

	public String getAccessLayerId() {
		return accessLayerId;
	}

	public void setAccessLayerId(String accessLayerId) {
		this.accessLayerId = accessLayerId;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	public String getAccessLayerIp() {
		return accessLayerIp;
	}

	public void setAccessLayerIp(String accessLayerIp) {
		this.accessLayerIp = accessLayerIp;
	}

	public Integer getAccessLayerPort() {
		return accessLayerPort;
	}

	public void setAccessLayerPort(Integer accessLayerPort) {
		this.accessLayerPort = accessLayerPort;
	}
	
}
