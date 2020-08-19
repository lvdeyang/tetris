package com.sumavision.tetris.cs.bak;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum VersionSendType {
	BROAD_FILE("文件发布"),
	BROAD_LIVE("直播发布"),
	DOWNLOAD_FILE("文件下载发布"),
	Update("目录同步");
	
	private String name;
	
	public String getName() {
		return name;
	}

	private VersionSendType(String name) {
		this.name = name;
	}
	
	public static VersionSendType fromName(String name) throws Exception{
		VersionSendType[] values = VersionSendType.values();
		for (VersionSendType value : values) {
			if (value.getName().equals(name)) {
				return value;
			}
		}
		throw new ErrorTypeException("versionSendType", name);
	}
}
