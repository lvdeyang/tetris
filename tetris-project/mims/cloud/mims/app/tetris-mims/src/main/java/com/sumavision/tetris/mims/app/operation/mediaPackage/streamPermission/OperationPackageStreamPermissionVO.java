package com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationPackageStreamPermissionVO extends AbstractBaseVO<OperationPackageStreamPermissionVO, OperationPackageStreamPermissionPO>{
	private Long packageId;
	
	private Long num;

	public Long getPackageId() {
		return packageId;
	}

	public OperationPackageStreamPermissionVO setPackageId(Long packageId) {
		this.packageId = packageId;
		return this;
	}

	public Long getNum() {
		return num;
	}

	public OperationPackageStreamPermissionVO setNum(Long num) {
		this.num = num;
		return this;
	}

	@Override
	public OperationPackageStreamPermissionVO set(OperationPackageStreamPermissionPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setPackageId(entity.getPackageId())
				.setNum(entity.getNum());
	}
}
