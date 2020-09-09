package com.sumavision.tetris.mims.app.media.upload;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaFileEquipmentPermissionVO extends AbstractBaseVO<MediaFileEquipmentPermissionVO, MediaFileEquipmentPermissionPO>{
	/** 资源id */
	private Long mediaId;
	
	/** 资源类型 */
	private String mediaType;
	
	/** 设备ip */
	private String equipmentIp;
	
	/** 设备上http地址 */
	private String equipmentHttpUrl;
	
	/** 设备上的绝对地址 */
	private String equipmentStoreUrl;

	public Long getMediaId() {
		return mediaId;
	}

	public MediaFileEquipmentPermissionVO setMediaId(Long mediaId) {
		this.mediaId = mediaId;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public MediaFileEquipmentPermissionVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getEquipmentIp() {
		return equipmentIp;
	}

	public MediaFileEquipmentPermissionVO setEquipmentIp(String equipmentIp) {
		this.equipmentIp = equipmentIp;
		return this;
	}

	public String getEquipmentHttpUrl() {
		return equipmentHttpUrl;
	}

	public MediaFileEquipmentPermissionVO setEquipmentHttpUrl(String equipmentHttpUrl) {
		this.equipmentHttpUrl = equipmentHttpUrl;
		return this;
	}

	public String getEquipmentStoreUrl() {
		return equipmentStoreUrl;
	}

	public MediaFileEquipmentPermissionVO setEquipmentStoreUrl(String equipmentStoreUrl) {
		this.equipmentStoreUrl = equipmentStoreUrl;
		return this;
	}

	@Override
	public MediaFileEquipmentPermissionVO set(MediaFileEquipmentPermissionPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setMediaId(entity.getMediaId())
				.setMediaType(entity.getMediaType())
				.setEquipmentIp(entity.getEquipmentIp())
				.setEquipmentHttpUrl(entity.getEquipmentHttpUrl())
				.setEquipmentStoreUrl(entity.getEquipmentStoreUrl());
	}
}
