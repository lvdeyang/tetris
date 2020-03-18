package com.sumavision.tetris.bvc.business.dispatch.bo;

import java.util.ArrayList;
import java.util.List;

public class StartUserDispatchBO {

	private String taskId = "";

	/** 仅在更新某个用户调度时使用 */
	private Long dispatchId = null;
	
	/** 通常为false；为true时，调度服务只进行记录而不实际调度 */
	private boolean recordOnly = false;

	private String meetingCode = "";

	private String userId = "";
	
	private List<ChannelBO> channels = new ArrayList<ChannelBO>();

	public String getTaskId() {
		return taskId;
	}

	public StartUserDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public Long getDispatchId() {
		return dispatchId;
	}

	public StartUserDispatchBO setDispatchId(Long dispatchId) {
		this.dispatchId = dispatchId;
		return this;
	}

	public boolean isRecordOnly() {
		return recordOnly;
	}

	public StartUserDispatchBO setRecordOnly(boolean recordOnly) {
		this.recordOnly = recordOnly;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StartUserDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getMeetingCode() {
		return meetingCode;
	}

	public StartUserDispatchBO setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
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
