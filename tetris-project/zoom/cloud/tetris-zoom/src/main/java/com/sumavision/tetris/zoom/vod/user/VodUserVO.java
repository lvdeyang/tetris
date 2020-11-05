package com.sumavision.tetris.zoom.vod.user;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VodUserVO extends AbstractBaseVO<VodUserVO, VodUserPO>{

	/** 呼叫用户id */
	private String srcUserId;
	
	/** 呼叫用户昵称 */
	private String srcNickname;
	
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
	
	/** 呗呼叫音频通道id */
	private String dstAudioChannelId;
	
	public String getSrcUserId() {
		return srcUserId;
	}

	public VodUserVO setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
		return this;
	}

	public String getSrcNickname() {
		return srcNickname;
	}

	public VodUserVO setSrcNickname(String srcNickname) {
		this.srcNickname = srcNickname;
		return this;
	}

	public String getDstUserId() {
		return dstUserId;
	}

	public VodUserVO setDstUserId(String dstUserId) {
		this.dstUserId = dstUserId;
		return this;
	}

	public String getDstNickname() {
		return dstNickname;
	}

	public VodUserVO setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
		return this;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public VodUserVO setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
		return this;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public VodUserVO setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
		return this;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public VodUserVO setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
		return this;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public VodUserVO setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
		return this;
	}

	@Override
	public VodUserVO set(VodUserPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcUserId(entity.getSrcUserId())
			.setSrcNickname(entity.getSrcNickname())
			.setDstUserId(entity.getDstUserId())
			.setDstNickname(entity.getDstNickname())
			.setDstBundleId(entity.getDstBundleId())
			.setDstLayerId(entity.getDstLayerId())
			.setDstVideoChannelId(entity.getDstVideoChannelId())
			.setDstAudioChannelId(entity.getDstAudioChannelId());
		return this;
	}
	
}
