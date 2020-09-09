package com.sumavision.tetris.business.push.vo;

public class SwitchScheduleVO {
	
	private String taskId;
	
	private String mediaType;
	
	private PushProgramVO program;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public PushProgramVO getProgram() {
		return program;
	}

	public void setProgram(PushProgramVO program) {
		this.program = program;
	}

}
