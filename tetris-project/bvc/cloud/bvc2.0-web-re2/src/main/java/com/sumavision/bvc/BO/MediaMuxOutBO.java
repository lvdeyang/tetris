package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONObject;


public class MediaMuxOutBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * uuid
	 */
	private String uuid;
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	/**
	 * 设备ID
	 */
	private String bundleId;
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId;
	
	/**
	 * 通道参数
	 */
	private JSONObject channel_param;

	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}		

	public String getLock_type() {
		return lock_type;
	}

	public void setLock_type(String lock_type) {
		this.lock_type = lock_type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public JSONObject getChannel_param() {
		return channel_param;
	}

	public void setChannel_param(JSONObject channel_param) {
		this.channel_param = channel_param;
	}
	
	

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "MediaMuxOutBO [taskId=" + taskId + ", lock_type=" + lock_type + ", uuid=" + uuid + ", layerId="
				+ layerId + ", bundleId=" + bundleId + ", channelId=" + channelId + ", channel_param=" + channel_param
				+ "]";
	}

	

}
