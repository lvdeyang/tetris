package com.sumavision.tetris.bvc.business.dispatch.bo;

public class StopBundleDispatchBO {

	private String taskId = "";

	private String userId = "";
	
	private String bundleId = "";
	
	public String getTaskId() {
		return taskId;
	}

	public StopBundleDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StopBundleDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public StopBundleDispatchBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

}
