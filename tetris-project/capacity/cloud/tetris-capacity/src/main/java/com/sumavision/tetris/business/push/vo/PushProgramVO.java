package com.sumavision.tetris.business.push.vo;

public class PushProgramVO {

	private String type;
	
	private PushFileVO file;
	
	private PushStreamVO stream;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PushFileVO getFile() {
		return file;
	}

	public void setFile(PushFileVO file) {
		this.file = file;
	}

	public PushStreamVO getStream() {
		return stream;
	}

	public void setStream(PushStreamVO stream) {
		this.stream = stream;
	}
	
}
