package com.sumavision.tetris.cs.channel.broad.ability;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class BroadAbilityBroadInfoVO extends AbstractBaseVO<BroadAbilityBroadInfoVO, BroadAbilityBroadInfoPO>{
	/** 频道id */
	private Long channelId;
	/** 预播发地址 */
	private String previewUrlIp;
	/** 预播发端口 */
	private String previewUrlPort;
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
		.setUserId(entity.getUserId());
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		BroadAbilityBroadInfoVO infoVO = (BroadAbilityBroadInfoVO)obj;
		return  this.userId == infoVO.getUserId()
				&& this.getPreviewUrlIp() != null
				&& this.getPreviewUrlIp().equals(infoVO.getPreviewUrlIp())
				&& this.getPreviewUrlPort() != null
				&& this.getPreviewUrlPort().equals(infoVO.getPreviewUrlPort());
	}
}
