package com.sumavision.tetris.bvc.business.dispatch.bo;

import java.util.ArrayList;
import java.util.List;

public class StartBundleDispatchBO {

	private String taskId = "";
	
	/** 通常为false；为true时，调度服务只进行记录而不实际调度 */
	private boolean recordOnly = false;

	private String meetingCode = "";

	private String userId = "";
	
	private String bundleId = "";
	
	private String layerId = "";
	
	private List<ChannelBO> channels = new ArrayList<ChannelBO>();

	public String getTaskId() {
		return taskId;
	}

	public StartBundleDispatchBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public boolean isRecordOnly() {
		return recordOnly;
	}

	public StartBundleDispatchBO setRecordOnly(boolean recordOnly) {
		this.recordOnly = recordOnly;
		return this;
	}

	public String getMeetingCode() {
		return meetingCode;
	}

	public StartBundleDispatchBO setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public StartBundleDispatchBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public StartBundleDispatchBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public StartBundleDispatchBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public List<ChannelBO> getChannels() {
		return channels;
	}

	public StartBundleDispatchBO setChannels(List<ChannelBO> channels) {
		this.channels = channels;
		return this;
	}

}
