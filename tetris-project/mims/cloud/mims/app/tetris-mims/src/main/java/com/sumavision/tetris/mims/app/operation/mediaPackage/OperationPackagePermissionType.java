package com.sumavision.tetris.mims.app.operation.mediaPackage;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OperationPackagePermissionType {
	PACKAGE_MEDIA_PERMISSION("套餐资源绑定关系"),
	PACKAGE_MEDIA_TYPE_PERMISSION("套餐资源类型绑定关系"),
	PACKAGE_STREAM_PERMISSION("套餐流量绑定关系");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private OperationPackagePermissionType(String name) {
		this.name = name;
	}
	
	public static OperationPackagePermissionType fromName(String name) throws Exception {
		OperationPackagePermissionType[] values = OperationPackagePermissionType.values();
		for (OperationPackagePermissionType operationPackagePermissionType : values) {
			if (operationPackagePermissionType.getName().equals(name)) return operationPackagePermissionType;
		}
		throw new ErrorTypeException("operationPackagePermissionType", name);
	}
}
