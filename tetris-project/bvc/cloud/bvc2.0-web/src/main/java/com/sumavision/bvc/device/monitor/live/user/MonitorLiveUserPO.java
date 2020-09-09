package com.sumavision.bvc.device.monitor.live.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 点播用户业务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 下午4:41:44
 */
@Entity
@Table(name = "BVC_MONITOR_LIVE_USER")
public class MonitorLiveUserPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/************
	 ***用户源****
	 ************/
	
	/** 用户号码 */
	private String srcUserno;
	
	/** 用户名 */
	private String srcUsername;
	
	/** 用户绑定编码器id */
	private String bundleId;
	
	/** 用户绑定编码器名称 */
	private String bundleName;
	
	/** 用户绑定编码器类型 */
	private String bundleType;
	
	/** 用户绑定编码器接入层 */
	private String layerId;
	
	/** 当视频源是设备时存通道id */
	private String videoChannelId;
	
	/** 当视频源是设备时存通道类型 */
	private String videoBaseType;
	
	/** 当视频源是设备时存通道名称 */
	private String videoChannelName;
	
	/** 当音频源是设备时存通道id */
	private String audioChannelId;
	
	/** 当音频源是设备时存通道类型 */
	private String audioBaseType;
	
	/** 当音频源是设备时存通道名称 */
	private String audioChannelName;
	
	/************
	 ***视频目的***
	 ************/
	
	/** 目标设备id */
	private String dstVideoBundleId;
	
	/** 目标设备名称 */
	private String dstVideoBundleName;
	
	/** 目标设备类型 */
	private String dstVideoBundleType;
	
	/** 目标设备接入层id */
	private String dstVideoLayerId;
	
	/** 目标视频通道 */
	private String dstVideoChannelId;
	
	/** 目标视频通道类型 */
	private String dstVideoBaseType;
	
	/** 目标视频通道名称 */
	private String dstVideoChannelName;
	
	/************
	 ***音频目的***
	 ************/
	
	/** 目标音频设备id */
	private String dstAudioBundleId;
	
	/** 目标音频设备名称 */
	private String dstAudioBundleName;
	
	/** 目标音频设备类型 */
	private String dstAudioBundleType;
	
	/** 目标音频设备接入层 */
	private String dstAudioLayerId;
	
	/** 目标音频通道id */
	private String dstAudioChannelId;
	
	/** 目标音频通道类型 */
	private String dstAudioBaseType;
	
	/** 目标音频通道名称 */
	private String dstAudioChannelName;
	
	/** 做点播设备业务的用户 */
	private Long userId;
	
	/** 做点播设备业务的用户名 */
	private String username;
	
	/** codec 模板 */
	private Long avTplId;
	
	/** codec模板档位 */
	private Long gearId;

	/** 目标设备类型 */
	private DstDeviceType dstDeviceType;
	
	/** 点播设备任务类型 */
	private LiveType type;

	@Column(name = "SRC_USERNO")
	public String getSrcUserno() {
		return srcUserno;
	}

	public void setSrcUserno(String srcUserno) {
		this.srcUserno = srcUserno;
	}

	@Column(name = "SRC_USERNAME")
	public String getSrcUsername() {
		return srcUsername;
	}

	public void setSrcUsername(String srcUsername) {
		this.srcUsername = srcUsername;
	}
	
	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "VIDEO_CHANNEL_ID")
	public String getVideoChannelId() {
		return videoChannelId;
	}

	public void setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
	}

	@Column(name = "VIDEO_BASE_TYPE")
	public String getVideoBaseType() {
		return videoBaseType;
	}

	public void setVideoBaseType(String videoBaseType) {
		this.videoBaseType = videoBaseType;
	}

	@Column(name = "VIDEO_CHANNEL_NAME")
	public String getVideoChannelName() {
		return videoChannelName;
	}

	public void setVideoChannelName(String videoChannelName) {
		this.videoChannelName = videoChannelName;
	}

	@Column(name = "AUDIO_CHANNEL_ID")
	public String getAudioChannelId() {
		return audioChannelId;
	}

	public void setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
	}

	@Column(name = "AUDIO_BASE_TYPE")
	public String getAudioBaseType() {
		return audioBaseType;
	}

	public void setAudioBaseType(String audioBaseType) {
		this.audioBaseType = audioBaseType;
	}

	@Column(name = "AUDIO_CHANNEL_NAME")
	public String getAudioChannelName() {
		return audioChannelName;
	}

	public void setAudioChannelName(String audioChannelName) {
		this.audioChannelName = audioChannelName;
	}

	@Column(name = "DST_VIDEO_BUNDLE_ID")
	public String getDstVideoBundleId() {
		return dstVideoBundleId;
	}

	public void setDstVideoBundleId(String dstVideoBundleId) {
		this.dstVideoBundleId = dstVideoBundleId;
	}

	@Column(name = "DST_VIDEO_BUNDLE_NAME")
	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public void setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
	}

	@Column(name = "DST_VIDEO_BUNDLE_TYPE")
	public String getDstVideoBundleType() {
		return dstVideoBundleType;
	}

	public void setDstVideoBundleType(String dstVideoBundleType) {
		this.dstVideoBundleType = dstVideoBundleType;
	}

	@Column(name = "DST_VIDEO_LAYER_ID")
	public String getDstVideoLayerId() {
		return dstVideoLayerId;
	}

	public void setDstVideoLayerId(String dstVideoLayerId) {
		this.dstVideoLayerId = dstVideoLayerId;
	}

	@Column(name = "DST_VIDEO_CHANNEL_ID")
	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	@Column(name = "DST_VIDEO_BASE_TYPE")
	public String getDstVideoBaseType() {
		return dstVideoBaseType;
	}

	public void setDstVideoBaseType(String dstVideoBaseType) {
		this.dstVideoBaseType = dstVideoBaseType;
	}

	@Column(name = "DST_VIDEO_CHANNEL_NAME")
	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public void setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
	}

	@Column(name = "DST_AUDIO_BUNDLE_ID")
	public String getDstAudioBundleId() {
		return dstAudioBundleId;
	}

	public void setDstAudioBundleId(String dstAudioBundleId) {
		this.dstAudioBundleId = dstAudioBundleId;
	}

	@Column(name = "DST_AUDIO_BUNDLE_NAME")
	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public void setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
	}

	@Column(name = "DST_AUDIO_BUNDLE_TYPE")
	public String getDstAudioBundleType() {
		return dstAudioBundleType;
	}

	public void setDstAudioBundleType(String dstAudioBundleType) {
		this.dstAudioBundleType = dstAudioBundleType;
	}

	@Column(name = "DST_AUDIO_LAYER_ID")
	public String getDstAudioLayerId() {
		return dstAudioLayerId;
	}

	public void setDstAudioLayerId(String dstAudioLayerId) {
		this.dstAudioLayerId = dstAudioLayerId;
	}

	@Column(name = "DST_AUDIO_CHANNEL_ID")
	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	@Column(name = "DST_AUDIO_BASE_TYPE")
	public String getDstAudioBaseType() {
		return dstAudioBaseType;
	}

	public void setDstAudioBaseType(String dstAudioBaseType) {
		this.dstAudioBaseType = dstAudioBaseType;
	}

	@Column(name = "DST_AUDIO_CHANNEL_NAME")
	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public void setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "AVTPL_ID")
	public Long getAvTplId() {
		return avTplId;
	}

	public void setAvTplId(Long avTplId) {
		this.avTplId = avTplId;
	}

	@Column(name = "GEAR_ID")
	public Long getGearId() {
		return gearId;
	}

	public void setGearId(Long gearId) {
		this.gearId = gearId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DST_DEVICE_TYPE")
	public DstDeviceType getDstDeviceType() {
		return dstDeviceType;
	}

	public void setDstDeviceType(DstDeviceType dstDeviceType) {
		this.dstDeviceType = dstDeviceType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public LiveType getType() {
		return type;
	}

	public void setType(LiveType type) {
		this.type = type;
	}
	
	public MonitorLiveUserPO(){}
	
	public MonitorLiveUserPO(
			String srcUserno,
			String srcUsername,
			String bundleId,
			String bundleName,
			String bundleType,
			String layerId,
			String videoChannelId,
			String videoBaseType,
			String audioChannelId,
			String audioBaseType,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			Long userId,
			String username,
			Long avTplId,
			Long gearId,
			DstDeviceType dstDeviceType,
			LiveType type) throws Exception{
		
		this.setUpdateTime(new Date());
		
		this.srcUserno = srcUserno;
		this.srcUsername = srcUsername;
		this.bundleId = bundleId;
		this.bundleName = bundleName;
		this.bundleType = bundleType;
		this.layerId = layerId;
		this.videoChannelId = videoChannelId;
		this.videoBaseType = videoBaseType;
		this.videoChannelName = ChannelType.transChannelName(videoChannelId);
		
		if(audioChannelId != null){
			this.audioChannelId = audioChannelId;
			this.audioBaseType = audioBaseType;
			this.audioChannelName = ChannelType.transChannelName(audioChannelId);
		}
		
		this.dstVideoBundleId = dstVideoBundleId;
		this.dstVideoBundleName = dstVideoBundleName;
		this.dstVideoBundleType = dstVideoBundleType;
		this.dstVideoLayerId = dstVideoLayerId;
		this.dstVideoChannelId = dstVideoChannelId;
		this.dstVideoBaseType = dstVideoBaseType;
		this.dstVideoChannelName = ChannelType.transChannelName(dstVideoChannelId);
		
		if(dstAudioBundleId != null){
			this.dstAudioBundleId = dstAudioBundleId;
			this.dstAudioBundleName = dstAudioBundleName;
			this.dstAudioBundleType = dstAudioBundleType;
			this.dstAudioLayerId = dstAudioLayerId;
			this.dstAudioChannelId = dstAudioChannelId;
			this.dstAudioBaseType = dstAudioBaseType;
			this.dstAudioChannelName = ChannelType.transChannelName(dstAudioChannelId);
		}
		
		this.userId = userId;
		this.username = username;
		this.avTplId = avTplId;
		this.gearId = gearId;
		this.dstDeviceType = dstDeviceType;
		this.type = type;
	}
	
}
