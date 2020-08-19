package com.sumavision.bvc.device.monitor.record;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 监控录制<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午4:14:41
 */
@Entity
@Table(name = "BVC_MONITOR_RECORD")
public class MonitorRecordPO extends AbstractBasePO{
	
	/** 排期录制执行时间间隔 */
	public static final int SCHEDULING_INTERVAL = 60000;

	private static final long serialVersionUID = 1L;
	
	/** 录制视频源的类型 */
	private MonitorRecordSourceType videoType;
	
	/** 当录制的视频源是合屏时存合屏的uuid */
	private String combineVideoUuid;
	
	/** 当录制的视频源是合屏时存合屏的名称 */
	private String combineVideoName;
	
	/** 当录制的视频源是设备时存设备id */
	private String videoBundleId;
	
	/** 当录制的视频源是设备是存设备名称 */
	private String videoBundleName;
	
	/** 当录制的视频源是设备时存设备类型 */
	private String videoBundleType;
	
	/** 当录制的视频源是设备时存接入层id */
	private String videoLayerId;
	
	/** 当录制的视频源是设备时存通道id */
	private String videoChannelId;
	
	/** 当录制的视频源是设备时存通道类型 */
	private String videoBaseType;
	
	/** 当录制的视频是设备时存通道名称 */
	private String videoChannelName;
	
	/** 录制音频源的类型 */
	private MonitorRecordSourceType audioType;
	
	/** 当录制的音频源是混音时存混音uuid */
	private String combineAudioUuid;
	
	/** 当录制的音频源是混音时存混音名称 */
	private String combineAudioName;
	
	/** 当录制的音频源是设备时存设备id */
	private String audioBundleId;
	
	/** 当录制的音频源是设备是存设备名称 */
	private String audioBundleName;
	
	/** 当录制的音频源是设备时存设备类型 */
	private String audioBundleType;
	
	/** 当录制的音频源是设备时存设备接入层id */
	private String audioLayerId;
	
	/** 当录制的音频源是设备时存通道id */
	private String audioChannelId;
	
	/** 当录制的音频源是设备时存通道类型 */
	private String audioBaseType;

	/** 当录制的音频源是设备时存通道名称 */
	private String audioChannelName;
	
	/** 录制文件名称 */
	private String fileName;
	
	/** 文件预览地址 */
	private String previewUrl;
	
	/** 开始录制时间 */
	private Date startTime;
	
	/** 结束录制时间 */
	private Date endTime;
	
	/** 录制模式：手动录制或者排期 */
	private MonitorRecordMode mode;
	
	/** 录制状态：录制中，已完成 */
	private MonitorRecordStatus status;
	
	/** 做业务的用户id */
	private Long userId;
	
	/** 做业务的用户号码 */
	private String userno;
	
	/** 做业务的用户昵称 */
	private String nickname;
	
	/** 录制类型标识录制用户还是录制设备 */
	private MonitorRecordType type;
	
	/** 如果是录制用户，存录制用户的id */
	private Long recordUserId;
	
	/** 如果是录制用户，存录制用户的名称 */
	private String recordUsername;
	
	/** 如果是录制用户，存录制用户的号码 */
	private String recordUserno;
	
	/** codec 模板 */
	private Long avTplId;
	
	/** codec 模板档位 */
	private Long gearId;
	
	/** 文件存储位置 */
	private String storeLayerId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_TYPE")
	public MonitorRecordSourceType getVideoType() {
		return videoType;
	}

	public void setVideoType(MonitorRecordSourceType videoType) {
		this.videoType = videoType;
	}

	@Column(name = "COMBINE_VIDEO_UUID")
	public String getCombineVideoUuid() {
		return combineVideoUuid;
	}

	public void setCombineVideoUuid(String combineVideoUuid) {
		this.combineVideoUuid = combineVideoUuid;
	}
	
	@Column(name = "COMBINE_VIDEO_NAME")
	public String getCombineVideoName() {
		return combineVideoName;
	}

	public void setCombineVideoName(String combineVideoName) {
		this.combineVideoName = combineVideoName;
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
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_TYPE")
	public MonitorRecordSourceType getAudioType() {
		return audioType;
	}

	public void setAudioType(MonitorRecordSourceType audioType) {
		this.audioType = audioType;
	}

	@Column(name = "COMBINE_AUDIO_UUID")
	public String getCombineAudioUuid() {
		return combineAudioUuid;
	}

	public void setCombineAudioUuid(String combineAudioUuid) {
		this.combineAudioUuid = combineAudioUuid;
	}

	@Column(name = "COMBINE_AUDIO_NAME")
	public String getCombineAudioName() {
		return combineAudioName;
	}

	public void setCombineAudioName(String combineAudioName) {
		this.combineAudioName = combineAudioName;
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

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MODE")
	public MonitorRecordMode getMode() {
		return mode;
	}

	public void setMode(MonitorRecordMode mode) {
		this.mode = mode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public MonitorRecordStatus getStatus() {
		return status;
	}

	public void setStatus(MonitorRecordStatus status) {
		this.status = status;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USERNO")
	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	@Column(name = "NICKNAME")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public MonitorRecordType getType() {
		return type;
	}

	public void setType(MonitorRecordType type) {
		this.type = type;
	}

	@Column(name = "RECORD_USER_ID")
	public Long getRecordUserId() {
		return recordUserId;
	}

	public void setRecordUserId(Long recordUserId) {
		this.recordUserId = recordUserId;
	}

	@Column(name = "RECORD_USER_NAME")
	public String getRecordUsername() {
		return recordUsername;
	}

	public void setRecordUsername(String recordUsername) {
		this.recordUsername = recordUsername;
	}

	@Column(name = "RECORD_USERNO")
	public String getRecordUserno() {
		return recordUserno;
	}

	public void setRecordUserno(String recordUserno) {
		this.recordUserno = recordUserno;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "AV_TPL_ID")
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

	@Column(name = "STORE_LAYER_ID")
	public String getStoreLayerId() {
		return storeLayerId;
	}

	public void setStoreLayerId(String storeLayerId) {
		this.storeLayerId = storeLayerId;
	}
	
}
