package com.sumavision.tetris.mims.app.bo;

public class AdiBo {
	//文件名称
	private String fileName;
	//媒资uuid，唯一标识
	private String mediaId;
	//媒资格式 video audio
	private String format;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
