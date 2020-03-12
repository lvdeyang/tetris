package com.sumavision.tetris.bvc.business.dispatch.bo;

import java.util.ArrayList;
import java.util.List;

public class StartUserDispatchBO {

	private String taskId = "";

	private String userId = "";
	
	private List<ChannelBO> channels = new ArrayList<ChannelBO>();

	public String getTaskId() {
		return taskId;
	}

	public StartUserDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StartUserDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public List<ChannelBO> getChannels() {
		return channels;
	}

	public StartUserDispatchBO setChannels(List<ChannelBO> channels) {
		this.channels = channels;
		return this;
	}

}
