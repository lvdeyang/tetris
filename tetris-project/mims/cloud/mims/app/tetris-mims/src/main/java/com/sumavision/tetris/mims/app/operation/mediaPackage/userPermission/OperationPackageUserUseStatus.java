package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OperationPackageUserUseStatus {
	FRESH("未使用"),
	USING("正在使用"),
	USE_UP("使用完");
	
	private String name;

	public String getName() {
		return name;
	}

	private OperationPackageUserUseStatus(String name) {
		this.name = name;
	}
	
	public static OperationPackageUserUseStatus fromName(String name) throws Exception {
		OperationPackageUserUseStatus[] values = OperationPackageUserUseStatus.values();
		for (OperationPackageUserUseStatus value : values) {
			if (value.name.equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("packageUseStatus", name);
	}
}
