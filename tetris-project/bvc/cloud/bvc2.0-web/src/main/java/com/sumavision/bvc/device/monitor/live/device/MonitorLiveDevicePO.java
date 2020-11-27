package com.sumavision.bvc.device.monitor.live.device;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.live.LiveType;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 点播设备<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 下午3:08:08
 */
@Entity
@Table(name = "BVC_MONITOR_LIVE_DEVICE")
public class MonitorLiveDevicePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/************
	 ***视频源****
	 ************/
	
	/** 播放udp流时的播放地址 */
	private String udpUrl;
	
	/** 当视频源是设备时存设备id */
	private String videoBundleId;
	
	/** 当视频源是设备时存设备名称 */
	private String videoBundleName;
	
	/** 当视频源是设备时存设备类型 */
	private String videoBundleType;
	
	/** 当视频源是设备时存设备具体类型，如jv210 */
	private String videoDeviceModel;
	
	/** 当视频源是设备时存设备接入层id */
	private String videoLayerId;
	
	/** 当视频源是设备时存通道id */
	private String videoChannelId;
	
	/** 当视频源是设备时存通道类型 */
	private String videoBaseType;
	
	/** 当视频源是设备时存通道名称 */
	private String videoChannelName;
	
	/************
	 ***音频源****
	 ************/
	
	/** 当音频源是设备时存设备id */
	private String audioBundleId;
	
	/** 当音频源是设备时存设备名称 */
	private String audioBundleName;
	
	/** 当音频源是设备时存设备类型 */
	private String audioBundleType;
	
	/** 当音频源是设备时存设备具体类型，如jv210 */
	private String audioDeviceModel;
	
	/** 当音频源是设备时存设备接入层 */
	private String audioLayerId;
	
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
	
	/** 目标设备具体类型 */
	private String dstVideoDeviceModel;
	
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

	/** 目标音频设备具体类型 */
	private String dstAudioDeviceModel;
	
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
	
	/** codec 模板 */
	private Long avTplId;
	
	/** codec模板档位 */
	private Long gearId;
	
	/** 目标设备类型 */
	private DstDeviceType dstDeviceType;
	
	/** 点播设备任务类型 */
	private LiveType type;
	
	/** osd显示 */
	private Long osdId;
	
	/** osd创建用户 */
	private String osdUsername;
	
	/** 使用MonitorRecordStatus中的RUN代表在执行，STOP表示停止*/
	private MonitorRecordStatus status;

	@Enumerated(value=EnumType.STRING)
	@Column(name="STATUS")
	public MonitorRecordStatus getStatus() {
		return status;
	}

	public MonitorLiveDevicePO setStatus(MonitorRecordStatus status) {
		this.status = status;
		return this;
	}

	@Column(name = "UDP_URL")
	public String getUdpUrl() {
		return udpUrl;
	}

	public void setUdpUrl(String udpUrl) {
		this.udpUrl = udpUrl;
	}

	@Column(name = "VIDEO_BUNDLE_ID")
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public void setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
	}

	@Column(name = "VIDEO_BUNDLE_NAME")
	public String getVideoBundleName() {
		return videoBundleName;
	}

	public void setVideoBundleName(String videoBundleName) {
		this.videoBundleName = videoBundleName;
	}

	@Column(name = "VIDEO_BUNDLE_TYPE")
	public String getVideoBundleType() {
		return videoBundleType;
	}

	public void setVideoBundleType(String videoBundleType) {
		this.videoBundleType = videoBundleType;
	}

	@Column(name = "VIDEO_DEVICE_MODEL")
	public String getVideoDeviceModel() {
		return videoDeviceModel;
	}

	public void setVideoDeviceModel(String videoDeviceModel) {
		this.videoDeviceModel = videoDeviceModel;
	}

	@Column(name = "VIDEO_LAYER_ID")
	public String getVideoLayerId() {
		return videoLayerId;
	}

	public void setVideoLayerId(String videoLayerId) {
		this.videoLayerId = videoLayerId;
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

	@Column(name = "AUDIO_BUNDLE_ID")
	public String getAudioBundleId() {
		return audioBundleId;
	}

	public void setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
	}

	@Column(name = "AUDIO_BUNDLE_NAME")
	public String getAudioBundleName() {
		return audioBundleName;
	}

	public void setAudioBundleName(String audioBundleName) {
		this.audioBundleName = audioBundleName;
	}

	@Column(name = "AUDIO_BUNDLE_TYPE")
	public String getAudioBundleType() {
		return audioBundleType;
	}

	public void setAudioBundleType(String audioBundleType) {
		this.audioBundleType = audioBundleType;
	}

	@Column(name = "AUDIO_DEVICE_MODEL")
	public String getAudioDeviceModel() {
		return audioDeviceModel;
	}

	public void setAudioDeviceModel(String audioDeviceModel) {
		this.audioDeviceModel = audioDeviceModel;
	}

	@Column(name = "AUDIO_LAYER_ID")
	public String getAudioLayerId() {
		return audioLayerId;
	}

	public void setAudioLayerId(String audioLayerId) {
		this.audioLayerId = audioLayerId;
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

	@Column(name = "DST_VIDEO_DEVICE_MODEL")
	public String getDstVideoDeviceModel() {
		return dstVideoDeviceModel;
	}

	public void setDstVideoDeviceModel(String dstVideoDeviceModel) {
		this.dstVideoDeviceModel = dstVideoDeviceModel;
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

	@Column(name = "DST_AUDIO_DEVICE_MODEL")
	public String getDstAudioDeviceModel() {
		return dstAudioDeviceModel;
	}

	public void setDstAudioDeviceModel(String dstAudioDeviceModel) {
		this.dstAudioDeviceModel = dstAudioDeviceModel;
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

	@Column(name = "OSD_ID")
	public Long getOsdId() {
		return osdId;
	}

	public void setOsdId(Long osdId) {
		this.osdId = osdId;
	}

	@Column(name = "OSD_USERNAME")
	public String getOsdUsername() {
		return osdUsername;
	}

	public void setOsdUsername(String osdUsername) {
		this.osdUsername = osdUsername;
	}
	
	public MonitorLiveDevicePO() {}
	
	public MonitorLiveDevicePO(
			String videoBundleId,
			String videoBundleName,
			String videoBundleType,
			String videoLayerId,
			String videoChannelId,
			String videoBaseType,
			String audioBundleId,
			String audioBundleName,
			String audioBundleType,
			String audioLayerId,
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
			Long avTplId,
			Long gearId,
			DstDeviceType dstDeviceType,
			LiveType type,
			Long osdId,
			String osdUsername) throws Exception{
		
		this.setStatus(MonitorRecordStatus.STOP);
		this.setUpdateTime(new Date());
		this.videoBundleId = videoBundleId;
		this.videoBundleName = videoBundleName;
		this.videoBundleType = videoBundleType;
		this.videoLayerId = videoLayerId;
		this.videoChannelId = videoChannelId;
		this.videoBaseType = videoBaseType;
		this.videoChannelName = ChannelType.transChannelName(videoChannelId);
		
		if(audioBundleId != null){
			this.audioBundleId = audioBundleId;
			this.audioBundleName = audioBundleName;
			this.audioBundleType = audioBundleType;
			this.audioLayerId = audioLayerId;
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
		this.avTplId = avTplId;
		this.gearId = gearId;
		this.dstDeviceType = dstDeviceType;
		this.type = type;
		this.osdId = osdId;
		this.osdUsername = osdUsername;
	}
	
}
