package com.sumavision.tetris.zoom.call.voice;

import javax.persistence.Column;

import com.sumavision.tetris.orm.po.AbstractBasePO;

public class CallVoicePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 呼叫用户id */
	private String srcUserId;
	
	/** 呼叫用户昵称 */
	private String srcNickname;
	
	/** 呼叫设备id */
	private String srcBundleId;
	
	/** 呼叫设备接入层id */
	private String srcLayerId;
	
	/** 呼叫音频通道id */
	private String srcAudioChannelId;
	
	/** 被呼叫用户id */
	private String dstUserId;
	
	/** 被呼叫用户昵称 */
	private String dstNickname;
	
	/** 被呼叫设备id */
	private String dstBundleId;
	
	/** 被呼叫设备接入层id */
	private String dstLayerId;
	
	/** 被呼叫音频通道id */
	private String dstAudioChannelId;

	@Column(name = "SRC_USER_ID")
	public String getSrcUserId() {
		return srcUserId;
	}

	public void setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
	}

	@Column(name = "SRC_NICKNAME")
	public String getSrcNickname() {
		return srcNickname;
	}

	public void setSrcNickname(String srcNickname) {
		this.srcNickname = srcNickname;
	}

	@Column(name = "SRC_BUNDLE_ID")
	public String getSrcBundleId() {
		return srcBundleId;
	}

	public void setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
	}

	@Column(name = "SRC_LAYER_ID")
	public String getSrcLayerId() {
		return srcLayerId;
	}

	public void setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
	}

	@Column(name = "SRC_AUDIO_CHANNEL_ID")
	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}

	@Column(name = "DST_USER_ID")
	public String getDstUserId() {
		return dstUserId;
	}

	public void setDstUserId(String dstUserId) {
		this.dstUserId = dstUserId;
	}

	@Column(name = "DST_NICKNAME")
	public String getDstNickname() {
		return dstNickname;
	}

	public void setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
	}

	@Column(name = "DST_BUNDLE_ID")
	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	@Column(name = "DST_LAYER_ID")
	public String getDstLayerId() {
		return dstLayerId;
	}

	public void setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
	}

	@Column(name = "DST_AUDIO_CHANNEL_ID")
	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

}
