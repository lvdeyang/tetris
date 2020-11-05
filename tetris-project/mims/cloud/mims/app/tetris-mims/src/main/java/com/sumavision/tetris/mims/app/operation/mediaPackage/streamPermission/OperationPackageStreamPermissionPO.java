package com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_PACKAGE_STREAM_PERMISSION")
public class OperationPackageStreamPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 套餐id */
	private Long packageId;
	
	/** 流量数 */
	private Long num;

	@Column(name = "PACKAGE_ID")
	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	@Column(name = "NUM")
	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}
}
