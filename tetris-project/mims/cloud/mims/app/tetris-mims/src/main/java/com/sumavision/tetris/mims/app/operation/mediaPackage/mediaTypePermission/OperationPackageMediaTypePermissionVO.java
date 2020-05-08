package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationPackageMediaTypePermissionVO extends AbstractBaseVO<OperationPackageMediaTypePermissionVO, OperationPackageMediaTypePermissionPO>{
	private Long packageId;
	
	private String mediaType;
	
	private Long num;

	public Long getPackageId() {
		return packageId;
	}

	public OperationPackageMediaTypePermissionVO setPackageId(Long packageId) {
		this.packageId = packageId;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public OperationPackageMediaTypePermissionVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public Long getNum() {
		return num;
	}

	public OperationPackageMediaTypePermissionVO setNum(Long num) {
		this.num = num;
		return this;
	}

	@Override
	public OperationPackageMediaTypePermissionVO set(OperationPackageMediaTypePermissionPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setPackageId(entity.getPackageId())
				.setMediaType(entity.getMediaType())
				.setNum(entity.getNum());
	}
}
