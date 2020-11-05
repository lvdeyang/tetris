package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestExchangeVO {
	/** 任务id */
	private String taskId;
	
	/** 媒体类型 */
	private String mediaType;
	
	/** 节目信息 */
	private BroadAbilityBroadRequestInputPrevVO program;

	public String getTaskId() {
		return taskId;
	}

	public BroadAbilityBroadRequestExchangeVO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public BroadAbilityBroadRequestExchangeVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevVO getProgram() {
		return program;
	}

	public BroadAbilityBroadRequestExchangeVO setProgram(BroadAbilityBroadRequestInputPrevVO program) {
		this.program = program;
		return this;
	}
}
