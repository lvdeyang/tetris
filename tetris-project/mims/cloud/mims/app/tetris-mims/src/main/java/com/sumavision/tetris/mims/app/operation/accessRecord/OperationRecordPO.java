package com.sumavision.tetris.mims.app.operation.accessRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackagePermissionType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_ACCESS_RECORD")
public class OperationRecordPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private Long userId;
	
	/** 媒资id */
	private Long mimsId;
	
	/** 媒资名称 */
	private String mimsName;

	/** 媒资uuid */
	private String mimsUuid;
	
	/** 媒资类型 */
	private String mimsType;
	
	/** 数量 */
	private Long num;
	
	/** 消耗的用户套餐关联关系的id */
	private Long permissionId;
	
	/** 消耗的套餐关联类型 */
	private OperationPackagePermissionType permissionType;

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Column(name = "MIMS_NAME")
	public String getMimsName() {
		return mimsName;
	}

	public void setMimsName(String mimsName) {
		this.mimsName = mimsName;
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

	@Column(name = "USER_PACKAGE_PERMISSION_ID")
	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MEDIA_PACKAGE_PERMISSION_TYPE")
	public OperationPackagePermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(OperationPackagePermissionType permissionType) {
		this.permissionType = permissionType;
	}
}
