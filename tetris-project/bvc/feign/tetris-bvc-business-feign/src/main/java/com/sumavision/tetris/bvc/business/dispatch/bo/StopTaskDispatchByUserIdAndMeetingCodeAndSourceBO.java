package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO {

	private String taskId = "";

	private String userId = "";

	private String meetingCode = "";

	private String sourceBundleId = "";

	private String sourceChannelId = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getMeetingCode() {
		return meetingCode;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
		return this;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
		return this;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
		return this;
	}

}
