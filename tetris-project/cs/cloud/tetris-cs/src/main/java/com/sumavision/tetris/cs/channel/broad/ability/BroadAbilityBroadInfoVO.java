package com.sumavision.tetris.cs.channel.broad.ability;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class BroadAbilityBroadInfoVO extends AbstractBaseVO<BroadAbilityBroadInfoVO, BroadAbilityBroadInfoPO>{
	/** 频道id */
	private Long channelId;
	/** 预播发地�? */
	private String previewUrlIp;
	/** 本地Ip */
	private String localIp;
	/** 预播发可用起始端�? */
	private String previewUrlPort;
	/** 预播发可用终止端�? */
	private String previewUrlEndPort;
	/** 媒资id */
	private Long mediaId;
	/** 预播发用户id */
	private Long userId;
	
	public Long getChannelId() {
		return channelId;
	}
	
	public BroadAbilityBroadInfoVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}
	
	public String getPreviewUrlIp() {
		return previewUrlIp;
	}
	
	public BroadAbilityBroadInfoVO setPreviewUrlIp(String previewUrlIp) {
		this.previewUrlIp = previewUrlIp;
		return this;
	}
	
	public String getPreviewUrlPort() {
		return previewUrlPort;
	}
	
	public BroadAbilityBroadInfoVO setPreviewUrlPort(String previewUrlPort) {
		this.previewUrlPort = previewUrlPort;
		return this;
	}
	
	public String getPreviewUrlEndPort() {
		return previewUrlEndPort;
	}

	public BroadAbilityBroadInfoVO setPreviewUrlEndPort(String previewUrlEndPort) {
		this.previewUrlEndPort = previewUrlEndPort;
		return this;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public BroadAbilityBroadInfoVO setMediaId(Long mediaId) {
		this.mediaId = mediaId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public BroadAbilityBroadInfoVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public BroadAbilityBroadInfoVO set(BroadAbilityBroadInfoPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setChannelId(entity.getChannelId())
		.setMediaId(entity.getMediaId())
		.setPreviewUrlIp(entity.getPreviewUrlIp())
		.setPreviewUrlPort(entity.getPreviewUrlPort())
		.setPreviewUrlEndPort(entity.getPreviewUrlEndPort())
		.setLocalIp(entity.getLocalIp())
		.setUserId(entity.getUserId());
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		BroadAbilityBroadInfoVO infoVO = (BroadAbilityBroadInfoVO)obj;
		return  this.userId == infoVO.getUserId()
				&& this.getPreviewUrlIp() != null
				&& this.getPreviewUrlIp().equals(infoVO.getPreviewUrlIp())
				&& this.getLocalIp() != null
				&& this.getLocalIp().equals(infoVO.getLocalIp())
				&& this.getPreviewUrlPort() != null
				&& this.getPreviewUrlPort().equals(infoVO.getPreviewUrlPort())
				&& this.getPreviewUrlEndPort() != null
				&& this.getPreviewUrlEndPort().equals(infoVO.getPreviewUrlEndPort());
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadInfoVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}
}
