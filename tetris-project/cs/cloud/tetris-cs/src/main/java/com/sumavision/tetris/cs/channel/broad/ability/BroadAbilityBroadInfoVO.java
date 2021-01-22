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
	private String outputType;
	private String rtmpUrl;
	/**码率控制*/
	private String rateCtrl;
	/**输出码率*/
	private String rate;
	
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
	
	

	public String getOutputType() {
		return outputType;
	}

	public BroadAbilityBroadInfoVO setOutputType(String outputType) {
		this.outputType = outputType;
		return this;
	}
	
	

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public BroadAbilityBroadInfoVO setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
		return this;
	}

	@Override
	public BroadAbilityBroadInfoVO set(BroadAbilityBroadInfoPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setChannelId(entity.getChannelId())
		.setMediaId(entity.getMediaId()==null?0:entity.getMediaId())
		.setPreviewUrlIp(entity.getPreviewUrlIp())
		.setPreviewUrlPort(entity.getPreviewUrlPort())
		.setPreviewUrlEndPort(entity.getPreviewUrlEndPort())
		.setLocalIp(entity.getLocalIp())
		.setOutputType(entity.getOutputType()==null?"":entity.getOutputType().getName())
		.setRtmpUrl(entity.getRtmpUrl())
		.setUserId(entity.getUserId())
		.setRate(entity.getRate())
		.setRateCtrl(entity.getRateCtrl());
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
				&& this.getOutputType()!=null
				&& this.getOutputType().equals(infoVO.getOutputType())
				&& this.getPreviewUrlPort().equals(infoVO.getPreviewUrlPort())
				&& this.getPreviewUrlEndPort() != null
				&& this.getRtmpUrl()!=null
				&& this.getRtmpUrl().equals(infoVO.getRtmpUrl())
				&& this.getPreviewUrlEndPort().equals(infoVO.getPreviewUrlEndPort());
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadInfoVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}

	public String getRateCtrl() {
		return rateCtrl;
	}

	public BroadAbilityBroadInfoVO setRateCtrl(String rateCtrl) {
		this.rateCtrl = rateCtrl;
		return this;
	}

	public String getRate() {
		return rate;
	}

	public BroadAbilityBroadInfoVO setRate(String rate) {
		this.rate = rate;
		return this;
	}
}
