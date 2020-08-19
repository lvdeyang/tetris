package com.sumavision.tetris.mims.app.operation.mediaPackage;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OperationPackageStatus {
	INVALID("旧"),
	AVAILABLE("新");
	
	 private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private OperationPackageStatus(String name) {
		this.setName(name);
	}
	
	public static OperationPackageStatus fromName(String name) throws Exception {
		OperationPackageStatus[] values = OperationPackageStatus.values();
		for (OperationPackageStatus value : values) {
			if (value.getName().equals(name)) {
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
