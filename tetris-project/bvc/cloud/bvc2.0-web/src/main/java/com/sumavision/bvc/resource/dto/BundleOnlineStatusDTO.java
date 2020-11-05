package com.sumavision.bvc.resource.dto;

import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;

public class BundleOnlineStatusDTO {

	private String bundleId;
	
	private ONLINE_STATUS onlineStatus;
	
	private String deviceIp;
	
	private Integer devicePort;
	
	/**如jv210等*/
	private String deviceModel;
	
	private String layerIp;
	
	//注意判空
	private NodeType layerType;
	
	//暂时无用
//	/**bundle_type,如VenusTerminal等*/
//	private String bundleType;
	
	public BundleOnlineStatusDTO(
			String bundleId,
			ONLINE_STATUS onlineStatus
			){
		this.bundleId = bundleId;
		this.setOnlineStatus(onlineStatus);
	}
	
	public BundleOnlineStatusDTO(
			String bundleId,
			ONLINE_STATUS onlineStatus,
			String deviceIp,
			Integer devicePort,
			String deviceModel,
			String layerIp,
			NodeType layerType
			){
		this.bundleId = bundleId;
		this.onlineStatus = onlineStatus;
		this.deviceIp = deviceIp;
		this.devicePort = devicePort;
		this.deviceModel = deviceModel;
		this.layerIp = layerIp;
		this.layerType = layerType;
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

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getLayerIp() {
		return layerIp;
	}

	public void setLayerIp(String layerIp) {
		this.layerIp = layerIp;
	}

	public NodeType getLayerType() {
		return layerType;
	}

	public void setLayerType(NodeType layerType) {
		this.layerType = layerType;
	}
		
}
