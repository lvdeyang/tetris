package com.sumavision.bvc.control.system.vo;

public class LayerVO {
	private String layerId;
	private String layerIp;
	private String type;
	private String typeShowName;
	private String version;
	public String getLayerId() {
		return layerId;
	}
	public LayerVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}
	public String getLayerIp() {
		return layerIp;
	}
	public LayerVO setLayerIp(String layerIpString) {
		this.layerIp = layerIpString;
		return this;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeShowName() {
		return typeShowName;
	}
	public void setTypeShowName(String typeShowName) {
		this.typeShowName = typeShowName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
