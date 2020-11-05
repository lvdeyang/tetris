package com.sumavision.tetris.zoom.call.user;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CallUserVO extends AbstractBaseVO<CallUserVO, CallUserPO>{

	/** 呼叫用户id */
	private String srcUserId;
	
	/** 呼叫用户昵称 */
	private String srcNickname;
	
	/** 呼叫用户设备id */
	private String srcBundleId;
	
	/** 呼叫用户设备接入层id */
	private String srcLayerId;
	
	/** 呼叫用户视频通道id */
	private String srcVideoChannelId;
	
	/** 呼叫用户音频通道id */
	private String srcAudioChannelId;
	
	/** 被呼叫用户id */
	private String dstUserId;
	
	/** 被呼叫用户昵称 */
	private String dstNickname;
	
	/** 被呼叫设备id */
	private String dstBundleId;
	
	/** 被呼叫设备接入层id */
	private String dstLayerId;
	
	/** 被呼叫视频通道id */
	private String dstVideoChannelId;
	
	/** 被呼叫音频通道id */
	private String dstAudioChannelId;
	
	/** 呼叫状态 */
	private String status;

	/** 呼叫类型 */
	private String type;
	
	public String getSrcUserId() {
		return srcUserId;
	}

	public CallUserVO setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
		return this;
	}

	public String getSrcNickname() {
		return srcNickname;
	}

	public CallUserVO setSrcNickname(String srcNickname) {
		this.srcNickname = srcNickname;
		return this;
	}

	public String getSrcBundleId() {
		return srcBundleId;
	}

	public CallUserVO setSrcBundleId(String srcBundleId) {
		this.srcBundleId = srcBundleId;
		return this;
	}

	public String getSrcLayerId() {
		return srcLayerId;
	}

	public CallUserVO setSrcLayerId(String srcLayerId) {
		this.srcLayerId = srcLayerId;
		return this;
	}

	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public CallUserVO setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
		return this;
	}

	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public CallUserVO setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
		return this;
	}

	public String getDstUserId() {
		return dstUserId;
	}

	public CallUserVO setDstUserId(String dstUserId) {
		this.dstUserId = dstUserId;
		return this;
	}

	public String getDstNickname() {
		return dstNickname;
	}

	public CallUserVO setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
		return this;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public CallUserVO setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
		return this;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public CallUserVO setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
		return this;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public CallUserVO setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
		return this;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public CallUserVO setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public CallUserVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getType() {
		return type;
	}

	public CallUserVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public CallUserVO set(CallUserPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcUserId(entity.getSrcUserId())
			.setSrcNickname(entity.getSrcNickname())
			.setSrcBundleId(entity.getSrcBundleId())
			.setSrcLayerId(entity.getSrcLayerId())
			.setSrcVideoChannelId(entity.getSrcVideoChannelId())
			.setSrcAudioChannelId(entity.getSrcAudioChannelId())
			.setDstUserId(entity.getDstUserId())
			.setDstNickname(entity.getDstNickname())
			.setDstBundleId(entity.getDstBundleId())
			.setDstLayerId(entity.getDstLayerId())
			.setDstVideoChannelId(entity.getDstVideoChannelId())
			.setDstAudioChannelId(entity.getDstAudioChannelId())
			.setStatus(entity.getStatus().toString())
			.setType(entity.getType().toString());
		return this;
	}
	
}
