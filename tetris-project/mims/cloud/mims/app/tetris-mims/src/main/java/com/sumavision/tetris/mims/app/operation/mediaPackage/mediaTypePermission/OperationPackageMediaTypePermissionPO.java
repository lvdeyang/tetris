package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_PACKAGE_MEDIA_TYPE_PERMISSION")
public class OperationPackageMediaTypePermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 套餐id */
	private Long packageId;
	
	/** 媒资类型 */
	private String mediaType;
	
	/** 数量 */
	private Long num;

	@Column(name = "PACKAGE_ID")
	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	@Column(name = "MEDIA_TYPE")
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Column(name = "NUM")
	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}
}
