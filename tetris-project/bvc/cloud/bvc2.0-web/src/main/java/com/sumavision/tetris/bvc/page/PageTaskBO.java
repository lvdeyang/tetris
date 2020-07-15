package com.sumavision.tetris.bvc.page;

import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;

/**
 * 播放器任务列表<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月27日 下午2:59:23
 */
public class PageTaskBO {
	
	private BusinessInfoType businessType = BusinessInfoType.NONE;

	/** 业务id，
	 * BASIC_COMMAND等指挥业务为“指挥id-观看对象userId”，
	 * COMMAND_FORWARD_xxx等指挥转发时为“指挥id-转发PO的id”，
	 * USER_CALL和USER_VOICE时为UserLiveCallPO的id
	 * PLAY_FILE时为resourceFileId，可能出现重复
	 * PLAY_USER和PLAY_DEVICE等点播任务时为CommandVodPO的id
	 */
	private String businessId;
	
	private String videoForwardUuid;
	
	private String audioForwardUuid;

	/** 业务名称，用于显示在播放器顶端，对应播放器信息中的businessInfo */
	private String businessName;

	/** 文件、录像的播放地址 */
	private String playUrl;
	
	/** osd id */
	private Long osdId;
	
	/** osd 名称 */
	private String osdName;
	
	/***********
	 * 源信息 *
	 **********/
	
	/** 源视频设备层节点id */
	private String srcVideoLayerId;
	
	/** 源视频设备id */
	private String srcVideoBundleId;
	
	/** 源设备视频通道id */
	private String srcVideoChannelId;
	
	/** 源音频设备层节点id */
	private String srcAudioLayerId;
	
	/** 源音频设备id */
	private String srcAudioBundleId;
	
	/** 源设备音频通道id */
	private String srcAudioChannelId;

	public BusinessInfoType getBusinessInfoType() {
		return businessType;
	}

	public void setBusinessInfoType(BusinessInfoType businessType) {
		this.businessType = businessType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getVideoForwardUuid() {
		return videoForwardUuid;
	}

	public void setVideoForwardUuid(String videoForwardUuid) {
		this.videoForwardUuid = videoForwardUuid;
	}

	public String getAudioForwardUuid() {
		return audioForwardUuid;
	}

	public void setAudioForwardUuid(String audioForwardUuid) {
		this.audioForwardUuid = audioForwardUuid;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public Long getOsdId() {
		return osdId;
	}

	public void setOsdId(Long osdId) {
		this.osdId = osdId;
	}

	public String getOsdName() {
		return osdName;
	}

	public void setOsdName(String osdName) {
		this.osdName = osdName;
	}
	
	public String getSrcVideoLayerId() {
		return srcVideoLayerId;
	}

	public void setSrcVideoLayerId(String srcVideoLayerId) {
		this.srcVideoLayerId = srcVideoLayerId;
	}

	public String getSrcVideoBundleId() {
		return srcVideoBundleId;
	}

	public void setSrcVideoBundleId(String srcVideoBundleId) {
		this.srcVideoBundleId = srcVideoBundleId;
	}

	public String getSrcVideoChannelId() {
		return srcVideoChannelId;
	}

	public void setSrcVideoChannelId(String srcVideoChannelId) {
		this.srcVideoChannelId = srcVideoChannelId;
	}

	public String getSrcAudioLayerId() {
		return srcAudioLayerId;
	}

	public void setSrcAudioLayerId(String srcAudioLayerId) {
		this.srcAudioLayerId = srcAudioLayerId;
	}

	public String getSrcAudioBundleId() {
		return srcAudioBundleId;
	}

	public void setSrcAudioBundleId(String srcAudioBundleId) {
		this.srcAudioBundleId = srcAudioBundleId;
	}

	public String getSrcAudioChannelId() {
		return srcAudioChannelId;
	}

	public void setSrcAudioChannelId(String srcAudioChannelId) {
		this.srcAudioChannelId = srcAudioChannelId;
	}
	
	public PageTaskBO setByVod(CommandVodPO vod){
		this.setBusinessId(vod.getId().toString());
		//TODO:按照类型区分
		this.setBusinessName("点播" + vod.getSourceBundleName() + "设备");
		this.setBusinessInfoType(BusinessInfoType.PLAY_DEVICE);
		this.setSrcAudioBundleId(vod.getSourceBundleId());
		this.setSrcAudioChannelId(vod.getSourceAudioChannelId());
		this.setSrcAudioLayerId(vod.getSourceLayerId());
		this.setSrcVideoBundleId(vod.getSourceBundleId());
		this.setSrcVideoChannelId(vod.getSourceVideoChannelId());
		this.setSrcVideoLayerId(vod.getSourceLayerId());
		return this;
	}
	
	public PageTaskBO setSrcByForward(CommandGroupForwardPO forward){
//		this.setBusinessId(forward.getId().toString());
//		this.setBusinessName("点播" + forward.getSourceBundleName() + "设备");
//		this.setPlayerBusinessType(PlayerBusinessType.PLAY_DEVICE);
		this.setSrcAudioBundleId(forward.getAudioBundleId());
		this.setSrcAudioChannelId(forward.getAudioChannelId());
		this.setSrcAudioLayerId(forward.getAudioLayerId());
		this.setSrcVideoBundleId(forward.getVideoBundleId());
		this.setSrcVideoChannelId(forward.getVideoChannelId());
		this.setSrcVideoLayerId(forward.getVideoLayerId());
		return this;
	}
	
}
