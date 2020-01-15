package com.sumavision.bvc.device.group.bo;

public class MediaPushSetBO {

	private String taskId;
	
	private String uuid;
	
	private String file_source;
	
	private CodecParamBO codec_param = new CodecParamBO();

	public String getTaskId() {
		return taskId;
	}

	public MediaPushSetBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaPushSetBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getFile_source() {
		return file_source;
	}

	public MediaPushSetBO setFile_source(String file_source) {
		this.file_source = file_source;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public MediaPushSetBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}
	
}
