package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopTaskDispatchBO {

	private String taskId = "";
	
	private String dispatchId = "";

	private String userId = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopTaskDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopTaskDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getDispatchId() {
		return dispatchId;
	}

	public StopTaskDispatchBO setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
		return this;
	}

}
