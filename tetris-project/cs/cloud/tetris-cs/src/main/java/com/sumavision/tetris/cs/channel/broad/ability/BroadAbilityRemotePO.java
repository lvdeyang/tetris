package com.sumavision.tetris.cs.channel.broad.ability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_ABILITY_REMOTE_INFO")
public class BroadAbilityRemotePO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 频道id */
	private Long channelId;
	
	/** 流程id */
	private String processInstanceId;
	
	/** 回调url */
	private String stopCallbackUrl;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "STOP_CALLBACK_URL")
	public String getStopCallbackUrl() {
		return stopCallbackUrl;
	}

	public void setStopCallbackUrl(String stopCallbackUrl) {
		this.stopCallbackUrl = stopCallbackUrl;
	}
}
