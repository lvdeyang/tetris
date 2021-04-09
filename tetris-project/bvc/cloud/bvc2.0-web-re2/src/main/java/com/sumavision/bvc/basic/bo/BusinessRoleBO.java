package com.sumavision.bvc.basic.bo;

import java.util.List;

public class BusinessRoleBO {

	private Long roleId;
	
	/** 是否是虚拟设备 */
	private boolean isVirtualDevice;
	
	/** bundleId,layerId,channelId当角色是虚拟设备才需要传,先放着 */
	private String bundleId;
	
	private String layerId;
	
	private String channelId;
	
	private List<BusinessBundleBO> bundles;

	public Long getRoleId() {
		return roleId;
	}

	public BusinessRoleBO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public boolean isVirtualDevice() {
		return isVirtualDevice;
	}

	public BusinessRoleBO setVirtualDevice(boolean isVirtualDevice) {
		this.isVirtualDevice = isVirtualDevice;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public BusinessRoleBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public BusinessRoleBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public BusinessRoleBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public List<BusinessBundleBO> getBundles() {
		return bundles;
	}

	public BusinessRoleBO setBundles(List<BusinessBundleBO> bundles) {
		this.bundles = bundles;
		return this;
	}
	
}
