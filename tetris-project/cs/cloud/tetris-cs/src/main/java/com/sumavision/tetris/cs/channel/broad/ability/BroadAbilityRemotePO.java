package com.sumavision.tetris.cs.channel.broad.ability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_ABILITY_REMOTE_INFO")
public class BroadAbilityRemotePO extends AbstractBasePO{

	/**
	 * 能力播发信息（channel频道--yjgb发布轮播推流补充信息）
	 */
	private static final long serialVersionUID = 1L;

	/** 频道id */
	private Long channelId;
	
	/** 流程id */
	private String processInstanceId;
	
	/** 回调url */
	private String stopCallbackUrl;
	
	/** 转换服务ip */
	private String deviceIp;
	
	/** 转码参数 */
	private String transcodeInfo;
	
	/** 推流方式 */
	private BroadStreamWay broadStreamWay;

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

	@Column(name = "DEVICE_IP")
	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Column(name = "TRANCODE_INFO")
	public String getTranscodeInfo() {
		return transcodeInfo;
	}

	public void setTranscodeInfo(String transcodeInfo) {
		this.transcodeInfo = transcodeInfo;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BROAD_STREAM_WAY")
	public BroadStreamWay getBroadStreamWay() {
		return broadStreamWay;
	}

	public void setBroadStreamWay(BroadStreamWay broadStreamWay) {
		this.broadStreamWay = broadStreamWay;
	}
}
