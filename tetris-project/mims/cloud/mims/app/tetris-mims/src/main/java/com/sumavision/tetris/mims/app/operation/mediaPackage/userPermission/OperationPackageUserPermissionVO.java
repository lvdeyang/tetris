package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationPackageUserPermissionVO extends AbstractBaseVO<OperationPackageUserPermissionVO, OperationPackageUserPermissionPO>{
	private Long packageId;
	
	private Long userId;
	
	private OperationPackageUserUseStatus status;
	
	private Long statisticId;
	
	private OperationPackageVO packageInfo;

	public Long getPackageId() {
		return packageId;
	}

	public OperationPackageUserPermissionVO setPackageId(Long packageId) {
		this.packageId = packageId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public OperationPackageUserPermissionVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public OperationPackageUserUseStatus getStatus() {
		return status;
	}

	public OperationPackageUserPermissionVO setStatus(OperationPackageUserUseStatus status) {
		this.status = status;
		return this;
	}

	public Long getStatisticId() {
		return statisticId;
	}

	public OperationPackageUserPermissionVO setStatisticId(Long statisticId) {
		this.statisticId = statisticId;
		return this;
	}

	public OperationPackageVO getPackageInfo() {
		return packageInfo;
	}

	public OperationPackageUserPermissionVO setPackageInfo(OperationPackageVO packageInfo) {
		this.packageInfo = packageInfo;
		return this;
	}

	@Override
	public OperationPackageUserPermissionVO set(OperationPackageUserPermissionPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setPackageId(entity.getPackageId())
				.setUserId(entity.getUserId())
				.setStatus(entity.getStatus());
	}
}
