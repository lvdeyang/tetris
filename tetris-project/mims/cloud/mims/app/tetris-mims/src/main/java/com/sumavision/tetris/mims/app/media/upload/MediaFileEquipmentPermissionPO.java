package com.sumavision.tetris.mims.app.media.upload;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_FILE_EQUIPMENT_PERMISSION", uniqueConstraints = {@UniqueConstraint(columnNames={"MEDIA_ID", "MEDIA_TYPE", "EQUIPMENT_IP"})})
public class MediaFileEquipmentPermissionPO extends AbstractBasePO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "MEDIA_TYPE")
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Column(name = "EQUIPMENT_IP")
	public String getEquipmentIp() {
		return equipmentIp;
	}

	public void setEquipmentIp(String equipmentIp) {
		this.equipmentIp = equipmentIp;
	}

	@Column(name = "EQUIPMENT_HTTP_URL")
	public String getEquipmentHttpUrl() {
		return equipmentHttpUrl;
	}

	public void setEquipmentHttpUrl(String equipmentHttpUrl) {
		this.equipmentHttpUrl = equipmentHttpUrl;
	}

	@Column(name = "EQUIPMENT_STORE_URL")
	public String getEquipmentStoreUrl() {
		return equipmentStoreUrl;
	}

	public void setEquipmentStoreUrl(String equipmentStoreUrl) {
		this.equipmentStoreUrl = equipmentStoreUrl;
	}
}
