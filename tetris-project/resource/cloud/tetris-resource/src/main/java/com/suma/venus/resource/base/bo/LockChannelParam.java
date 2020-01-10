package com.suma.venus.resource.base.bo;

import java.util.List;

public class LockChannelParam {

	private Long userId;
	
	private Long taskId;
	
	private List<ChannelBody> channels;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public List<ChannelBody> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelBody> channels) {
		this.channels = channels;
	}
	
}
