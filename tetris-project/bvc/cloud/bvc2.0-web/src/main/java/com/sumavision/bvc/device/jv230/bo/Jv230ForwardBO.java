package com.sumavision.bvc.device.jv230.bo;

import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;

/**
* @ClassName: 协议层jv230转发(video+audio)
* @author wjw 
* @date 2018年8月24日 下午3:40:19 
*
 */
public class Jv230ForwardBO {

	/** 任务id，这里暂时空着 */
	private String taskId = "";
	
	/** 锁类型write/read */
	private String lock_type;
	
	/** Jv230执行层layerId */
	private String layerId;
	
	/** Jv230设备bundleId */
	private String bundleId;
	
	/** Jv230解码通道channelId */
	private String channelId;
	
	private Jv230ChannelParamBO channel_param;

	public String getTaskId() {
		return taskId;
	}

	public Jv230ForwardBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLock_type() {
		return lock_type;
	}

	public Jv230ForwardBO setLock_type(String lock_type) {
		this.lock_type = lock_type;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public Jv230ForwardBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public Jv230ForwardBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public Jv230ForwardBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public Jv230ChannelParamBO getChannel_param() {
		return channel_param;
	}

	public Jv230ForwardBO setChannel_param(Jv230ChannelParamBO channel_param) {
		this.channel_param = channel_param;
		return this;
	}	
	
	public Jv230ForwardBO set(Jv230ChannelPO jv230ChannelPO, Jv230ChannelParamBO channel_param){
		this.setLock_type("write")
			.setLayerId(jv230ChannelPO.getLayerId())
			.setBundleId(jv230ChannelPO.getBundleId())
			.setChannelId(jv230ChannelPO.getChannelId())
			.setChannel_param(channel_param);
		
		return this;
	}
}
