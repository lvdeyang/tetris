package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopTaskDispatchByUserIdAndSourceBO {

	private String taskId = "";

	private String userId = "";

	private String sourceBundleId = "";

	private String sourceChannelId = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopTaskDispatchByUserIdAndSourceBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopTaskDispatchByUserIdAndSourceBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public StopTaskDispatchByUserIdAndSourceBO setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
		return this;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public StopTaskDispatchByUserIdAndSourceBO setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
		return this;
	}

}
