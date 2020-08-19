package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopUserDispatchBO {

	private String taskId = "";

	private String userId = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopUserDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopUserDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

}
