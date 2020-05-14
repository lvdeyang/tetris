package com.sumavision.tetris.mims.app.media.upload;

public class MediaFileEquipmentPermissionPO {
	private Long mediaId;
	
	private String mediaType;
	
	private String equipmentIp;
	
	private String equipmentHttpUrl;
	
	private String equipmentStoreUrl;

	public Long getMediaId() {
		return mediaId;
	}

	public MediaFileEquipmentPermissionPO setMediaId(Long mediaId) {
		this.mediaId = mediaId;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public MediaFileEquipmentPermissionPO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getEquipmentIp() {
		return equipmentIp;
	}

	public MediaFileEquipmentPermissionPO setEquipmentIp(String equipmentIp) {
		this.equipmentIp = equipmentIp;
		return this;
	}

	public String getEquipmentHttpUrl() {
		return equipmentHttpUrl;
	}

	public MediaFileEquipmentPermissionPO setEquipmentHttpUrl(String equipmentHttpUrl) {
		this.equipmentHttpUrl = equipmentHttpUrl;
		return this;
	}

	public String getEquipmentStoreUrl() {
		return equipmentStoreUrl;
	}

	public MediaFileEquipmentPermissionPO setEquipmentStoreUrl(String equipmentStoreUrl) {
		this.equipmentStoreUrl = equipmentStoreUrl;
		return this;
	}
}
