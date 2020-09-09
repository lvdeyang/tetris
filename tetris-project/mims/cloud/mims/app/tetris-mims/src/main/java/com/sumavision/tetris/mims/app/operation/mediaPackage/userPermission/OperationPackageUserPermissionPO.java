package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_PACKAGE_USER_PERMISSION")
public class OperationPackageUserPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 套餐id */
	private Long packageId;
	
	/** 用户id */
	private Long userId;
	
	/** 使用状态 */
	private OperationPackageUserUseStatus status;
	
	/** 结算策略id */
	private Long statisticId;

	@Column(name = "PACKAGE_ID")
	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public OperationPackageUserUseStatus getStatus() {
		return status;
	}

	public void setStatus(OperationPackageUserUseStatus status) {
		this.status = status;
	}

	@Column(name = "STATISTIC_ID")
	public Long getStatisticId() {
		return statisticId;
	}

	public void setStatisticId(Long statisticId) {
		this.statisticId = statisticId;
	}
}
