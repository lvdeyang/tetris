package com.sumavision.tetris.cs.bak;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name="TETRIS_CS_SEND_RESOURCE")
public class ResourceSendPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 媒资名称 */
	private String name;
	
	/** 媒资时常 */
	private String time;
	
	/** 媒资在mims中的uuid */
	private String mimsUuid;
	
	/** 媒资program目录结构中的父目录 */
	private Long parentId;
	
	/** 频道id */
	private Long channelId;
	
	/** 媒资预览地址 */
	private String previewUrl;
	
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

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TIME")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "MIMS_UUID")
	public String getMimsUuid() {
		return mimsUuid;
	}

	public void setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
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
}
