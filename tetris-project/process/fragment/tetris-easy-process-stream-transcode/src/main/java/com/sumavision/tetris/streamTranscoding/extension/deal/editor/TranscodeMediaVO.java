package com.sumavision.tetris.streamTranscoding.extension.deal.editor;

public class TranscodeMediaVO {
	private String uuid;
	
	private Long startTime;
	
	private Long endTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
