package com.sumavision.tetris.cs.program;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ScreenContentType {
	TERMINAL_MIMS("仓库资源", "file", "file"),
//	AVIDEO("音视频资源", "avideo", "mims"),
	ABILITY_AUDIO("音频资源", "audio", "file"),
	ABILITY_VIDEO("视频资源", "video", "file"),
	ABILITY_PICTURE("图片资源", "picture", "mims"),
	TEXT("自定义文字", "text", "text"),
	TIME("时间", "time", "time");
	
	private String name;
	private String value;
	private String type;
	
	private ScreenContentType(String name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public String getType() {
		return type;
	}

	public static ScreenContentType fromName(String name) throws Exception {
		ScreenContentType[] values = ScreenContentType.values();
		for (ScreenContentType screenContentType : values) {
			if (screenContentType.getName().equals(name)) {
				return screenContentType;
			}
		}
		throw new ErrorTypeException("ScreenContentTpe name", name);
	}
	
	public static ScreenContentType fromValue(String value) throws Exception {
		ScreenContentType[] values = ScreenContentType.values();
		for (ScreenContentType screenContentType : values) {
			if (screenContentType.getValue().equals(value)) {
				return screenContentType;
			}
		}
		throw new ErrorTypeException("ScreenContentType value", value);
	}
}
