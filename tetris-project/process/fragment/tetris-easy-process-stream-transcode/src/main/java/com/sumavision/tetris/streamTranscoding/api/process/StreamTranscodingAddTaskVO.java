package com.sumavision.tetris.streamTranscoding.api.process;

import com.sumavision.tetris.streamTranscoding.api.server.TaskVO;

public class StreamTranscodingAddTaskVO {
	private String assetPath;
	private boolean record;
	private String mediaType;
	private String recordCallback;
	private Integer progNum;
	private TaskVO task;
	
	public String getAssetPath() {
		return assetPath;
	}
	public void setAssetPath(String assetPath) {
		this.assetPath = assetPath;
	}
	public boolean isRecord() {
		return record;
	}
	public void setRecord(boolean record) {
		this.record = record;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getRecordCallback() {
		return recordCallback;
	}
	public void setRecordCallback(String recordCallback) {
		this.recordCallback = recordCallback;
	}
	public Integer getProgNum() {
		return progNum;
	}
	public void setProgNum(Integer progNum) {
		this.progNum = progNum;
	}
	public TaskVO getTask() {
		return task;
	}
	public void setTask(TaskVO task) {
		this.task = task;
	}
}
