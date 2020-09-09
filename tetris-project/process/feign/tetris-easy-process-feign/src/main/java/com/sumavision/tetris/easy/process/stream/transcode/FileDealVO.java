package com.sumavision.tetris.easy.process.stream.transcode;

public class FileDealVO {
	/** 是否直接把文件推流转码（使用流转码能力） */
	private Boolean ifFileStream = false;
	
	/** 文件位置 */
	private String fileUrl;
	
	/** 文件时长 */
	private Long duration;
	
	/** 是否需要文件转码（使用云转码和推流能力） */
	private Boolean ifMediaEdit = false;
	
	/** 文件转码参数 */
	private String transcodeJob;
	
	/** 文件转码模板 */
	private String param;
	
	/** 文件转码文件名 */
	private String name;
	
	/** 文件转码输出目录 */
	private String folderId;

	public Boolean getIfFileStream() {
		return ifFileStream;
	}

	public FileDealVO setIfFileStream(Boolean ifFileStream) {
		this.ifFileStream = ifFileStream;
		return this;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public FileDealVO setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public FileDealVO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public Boolean getIfMediaEdit() {
		return ifMediaEdit;
	}

	public FileDealVO setIfMediaEdit(Boolean ifMediaEdit) {
		this.ifMediaEdit = ifMediaEdit;
		return this;
	}

	public String getTranscodeJob() {
		return transcodeJob;
	}

	public FileDealVO setTranscodeJob(String transcodeJob) {
		this.transcodeJob = transcodeJob;
		return this;
	}

	public String getParam() {
		return param;
	}

	public FileDealVO setParam(String param) {
		this.param = param;
		return this;
	}

	public String getName() {
		return name;
	}

	public FileDealVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getFolderId() {
		return folderId;
	}

	public FileDealVO setFolderId(String folderId) {
		this.folderId = folderId;
		return this;
	}
}
