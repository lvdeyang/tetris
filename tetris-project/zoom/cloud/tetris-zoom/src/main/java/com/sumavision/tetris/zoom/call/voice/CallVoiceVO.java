package com.sumavision.tetris.zoom.call.voice;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CallVoiceVO extends AbstractBaseVO<CallVoiceVO, CallVoicePO>{

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
	
	public String getSrcUserId() {
		return srcUserId;
	}

	public CallVoiceVO setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
		return this;
	}

	public String getSrcNickname() {
		return srcNickname;
	}

	public CallVoiceVO setSrcNickname(String srcNickname) {
		this.srcNickname = srcNickname;
		return this;
	}

	public String getSrcBundleId() {
		return srcBundleId;
	}

	public CallVoiceVO setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
		return this;
	}

	public String getSrcLayerId() {
		return srcLayerId;
	}

	public CallVoiceVO setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
		return this;
	}

	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public CallVoiceVO setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
		return this;
	}

	public String getDstUserId() {
		return dstUserId;
	}

	public CallVoiceVO setDstUserId(String dstUserId) {
		this.dstUserId = dstUserId;
		return this;
	}

	public String getDstNickname() {
		return dstNickname;
	}

	public CallVoiceVO setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
		return this;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public CallVoiceVO setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
		return this;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public CallVoiceVO setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
		return this;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public CallVoiceVO setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
		return this;
	}

	@Override
	public CallVoiceVO set(CallVoicePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcUserId(entity.getSrcUserId())
			.setSrcNickname(entity.getSrcNickname())
			.setSrcBundleId(entity.getSrcBundleId())
			.setSrcLayerId(entity.getSrcBundleId())
			.setSrcAudioChannelId(entity.getSrcAudioChannelId())
			.setDstUserId(entity.getDstUserId())
			.setDstNickname(entity.getDstNickname())
			.setDstBundleId(entity.getDstBundleId())
			.setDstLayerId(entity.getDstLayerId())
			.setDstAudioChannelId(entity.getDstAudioChannelId());
		return this;
	}

}
