package com.sumavision.tetris.mims.app.media.compress;

public class FileCompressVO {
	private String uuid;
	private String path;
	private String uploadPathString;
	public String getUuid() {
		return uuid;
	}
	public FileCompressVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	public String getPath() {
		return path;
	}
	public FileCompressVO setPath(String path) {
		this.path = path;
		return this;
	}
	public String getUploadPathString() {
		return uploadPathString;
	}
	public FileCompressVO setUploadPathString(String uploadPathString) {
		this.uploadPathString = uploadPathString;
		return this;
	}
}
