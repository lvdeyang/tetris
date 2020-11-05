package com.sumavision.tetris.zoom.vod.device;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VodDeviceVO extends AbstractBaseVO<VodDeviceVO, VodDevicePO>{

	/** 用户id */
	private String userId;
	
	/** 用户昵称 */
	private String nickname;
	
	/** 目标设备接入层id */
	private String dstLayerId;
	
	/** 目标设备id */
	private String dstBundleId;
	
	/** 目标设备视频通道id */
	private String dstVideoChannelId;
	
	/** 目标设备音频通道id */
	private String dstAudioChannelId;
	
	public String getUserId() {
		return userId;
	}

	public VodDeviceVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public VodDeviceVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public String getDstLayerId() {
		return dstLayerId;
	}

	public VodDeviceVO setDstLayerId(String dstLayerId) {
		this.dstLayerId = dstLayerId;
		return this;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public VodDeviceVO setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
		return this;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public VodDeviceVO setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
		return this;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public VodDeviceVO setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
		return this;
	}

	@Override
	public VodDeviceVO set(VodDevicePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(entity.getUserId())
			.setNickname(entity.getNickname())
			.setDstBundleId(entity.getDstBundleId())
			.setDstLayerId(entity.getDstLayerId())
			.setDstVideoChannelId(entity.getDstVideoChannelId())
			.setDstAudioChannelId(entity.getDstAudioChannelId());
		return this;
	}

}
