package com.sumavision.tetris.cs.program;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_SCREEN")
public class ScreenPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 分屏id */
	private Long programId;
	
	/** 分屏位置 */
	private Long serialNum;
	
	/** 排单顺序 */
	private Long screenIndex;
	
	/** 内容类型 */
	private String contentType;
	
	/** 文本内容 */
	private String textContent;
	
	/** 媒资在cs中的id */
	private Long resourceId;
	
	/** 媒资在mims中的uuid */
	private String mimsUuid;
	
	/** 媒资名称 */
	private String name;
	
	/** 媒资类型 */
	private String type;
	
	/** 媒资的mimetype */
	private String mimetype;
	
	/** 媒资预览地址 */
	private String previewUrl;
	
	/** 媒资加密预览地址 */
	private String encryptionUrl;
	
	/** 媒资热度权重 */
	private Integer hotWeight;
	
	/** 媒资下载量 */
	private Integer downloadCount;
	
	/** 媒资时长 */
	private String duration;
	
	/** 媒资大小 */
	private String size;
	
	/** 媒资频点 */
	private String freq;
	
	/** 媒资音频pid */
	private String audioPid;
	
	/** 媒资视频pid */
	private String videoPid;
	
	/** 媒资音频类型 */
	private String audioType;
	
	/** 媒资视频类型 */
	private String videoType;
	
	/**是否必选节目*/
	private Boolean isRequired;
	
	private Date startTime;
	
	private Date endTime;
	
	@Column(name = "ISREQUIRED")
	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	@Column(name="PROGRAM_ID")
	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	@Column(name="SERIAL_NUM")
	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}
	
	@Column(name="SCREEN_INDEX")
	public void setScreenIndex(Long screenIndex) {
		this.screenIndex = screenIndex;
	}
	
	public Long getScreenIndex() {
		return screenIndex;
	}
	
	@Column(name = "CONTENT_TYPE")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "TEXT_CONTENT")
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	@Column(name="RESOURCE_ID")
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name="MIMS_UUID")
	public String getMimsUuid() {
		return mimsUuid;
	}

	public void setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "MIMETYPE")
	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name="PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "ENCRYPTION_URL")
	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public void setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
	}

	@Column(name = "HOT_WEIGHT")
	public Integer getHotWeight() {
		return hotWeight;
	}

	public void setHotWeight(Integer hotWeight) {
		this.hotWeight = hotWeight;
	}

	@Column(name = "DOWNLOAD_COUNT")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "SIZE")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "FREQ")
	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	@Column(name = "AUDIO_PID")
	public String getAudioPid() {
		return audioPid;
	}

	public void setAudioPid(String audioPid) {
		this.audioPid = audioPid;
	}

	@Column(name = "VIDEO_PID")
	public String getVideoPid() {
		return videoPid;
	}

	public void setVideoPid(String videoPid) {
		this.videoPid = videoPid;
	}
	
	@Column(name = "AUDIO_TYPE")
	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	@Column(name = "VIDEO_TYPE")
	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
