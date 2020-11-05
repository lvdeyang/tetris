package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_PACKAGE_MEDIA_PERMISSION")
public class OperationPackageMediaPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 套餐id */
	private Long packageId;
	
	/** 媒资名称 */
	private String mimsName;
	
	/** 媒资id */
	private Long mimsId;
	
	/** 媒资uuid */
	private String mimsUuid;
	
	/** 媒资类型 */
	private String mimsType;
	
	/** 数量 */
	private Long num;

	@Column(name = "PACKAGE_ID")
	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	
	@Column(name = "MIMS_NAME")
	public String getMimsName() {
		return mimsName;
	}

	public void setMimsName(String mimsName) {
		this.mimsName = mimsName;
	}

	@Column(name = "MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Column(name = "MIMS_UUID")
	public String getMimsUuid() {
		return mimsUuid;
	}

	public void setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
	}

	@Column(name = "MIMS_TYPE")
	public String getMimsType() {
		return mimsType;
	}

	public void setMimsType(String mimsType) {
		this.mimsType = mimsType;
	}

	@Column(name = "NUM")
	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}
}
