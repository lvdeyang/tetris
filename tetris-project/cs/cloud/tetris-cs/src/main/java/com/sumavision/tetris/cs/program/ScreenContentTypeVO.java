package com.sumavision.tetris.cs.program;

public class ScreenContentTypeVO {
	private String name;
	
	private String value;

	public String getName() {
		return name;
	}

	public ScreenContentTypeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ScreenContentTypeVO setValue(String value) {
		this.value = value;
		return this;
	}
	
	public ScreenContentTypeVO set(ScreenContentType contentType) {
		return this.setName(contentType.getName())
				.setValue(contentType.getValue());
	}
}
