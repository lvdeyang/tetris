package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationPackageMediaPermissionVO extends AbstractBaseVO<OperationPackageMediaPermissionVO, OperationPackageMediaPermissionPO>{
	
	private Long packageId;
	
	private String mimsName;
	
	private Long mimsId;
	
	private String mimsUuid;
	
	private String mimsType;
	
	private Long num;

	public Long getPackageId() {
		return packageId;
	}

	public OperationPackageMediaPermissionVO setPackageId(Long packageId) {
		this.packageId = packageId;
		return this;
	}

	public String getMimsName() {
		return mimsName;
	}

	public OperationPackageMediaPermissionVO setMimsName(String mimsName) {
		this.mimsName = mimsName;
		return this;
	}

	public Long getMimsId() {
		return mimsId;
	}

	public OperationPackageMediaPermissionVO setMimsId(Long mimsId) {
		this.mimsId = mimsId;
		return this;
	}

	public String getMimsUuid() {
		return mimsUuid;
	}

	public OperationPackageMediaPermissionVO setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
		return this;
	}

	public String getMimsType() {
		return mimsType;
	}

	public OperationPackageMediaPermissionVO setMimsType(String mimsType) {
		this.mimsType = mimsType;
		return this;
	}

	public Long getNum() {
		return num;
	}

	public OperationPackageMediaPermissionVO setNum(Long num) {
		this.num = num;
		return this;
	}

	@Override
	public OperationPackageMediaPermissionVO set(OperationPackageMediaPermissionPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setPackageId(entity.getPackageId())
				.setMimsName(entity.getMimsName())
				.setMimsId(entity.getMimsId())
				.setMimsType(entity.getMimsType())
				.setMimsUuid(entity.getMimsUuid())
				.setNum(entity.getNum());
	}

}
