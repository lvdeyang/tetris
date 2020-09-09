package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopTaskDispatchByUserIdAndMeetingCodeBO {

	private String taskId = "";

	private String userId = "";

	private String meetingCode = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getMeetingCode() {
		return meetingCode;
	}

	public StopTaskDispatchByUserIdAndMeetingCodeBO setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
		return this;
	}

}
