package com.sumavision.tetris.agent.vo;

public class RemotePullVO {

	private String status;
	
	private String layer_id;
	
	private String bundle_id;
	
	private String channel_id;

	public String getStatus() {
		return status;
	}

	public RemotePullVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getLayer_id() {
		return layer_id;
	}

	public RemotePullVO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public RemotePullVO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public RemotePullVO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}
	
}
