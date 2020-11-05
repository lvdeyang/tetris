package com.sumavision.tetris.cs.menu;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorVO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_MENU_RESOURCE")
public class CsResourcePO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 在mims的uuid */
	private String mimsUuid;
	
	/** 在cs父目录id */
	private Long parentId;
	
	/** 在cs父目录路径 */
	private String parentPath;
	
	/** 频道id */
	private Long channelId;
	
	/** 名称 */
	private String name;
	
	/** 预览路径 */
	private String previewUrl;
	
	/** 时长 */
	private String duration;
	
	/** 文件大小 */
	private String size;
	
	/** 媒资类型 */
	private String type;
	
	/** 媒资的mimeType */
	private String mimetype;
	
	/** 媒资下载量 */
	private Integer downloadCount;
	
	/** 媒资是否加密 */
	private String encryption;
	
	/** 媒资加密预览地址 */
	private String encryptionUrl;
	
	/** 媒资频点 */
	private String freq;
	
	/** 媒资音频pid */
	private String audioPid;
	
	/** 视频pid */
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

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
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

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "MIMSTYPE")
	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name = "DOWNLOAD_COUNT")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "IF_ENCRYPTION")
	public String getEncryption() {
		return encryption;
	}

	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}

	@Column(name = "ENCRYPTION_URL")
	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public void setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
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

	public CsResourcePO getFromAVideoVO(MediaAVideoVO media) {
		if (media != null) {
			this.setMimsUuid(media.getUuid());
			this.setName(media.getName());
			this.setType(media.getType());
			this.setMimetype(media.getMimetype());
			this.setDuration(media.getDuration());
			this.setSize(media.getSize());
			this.setPreviewUrl(media.getPreviewUrl());
			
			MediaFileEditorVO editorVO = media.getEditorInfo();
			if (editorVO != null) {
				String previewUrl = editorVO.getPreviewUrl();
				String duration = editorVO.getDuration();
				Long size = editorVO.getSize();
				if (previewUrl != null && !previewUrl.isEmpty()) this.setPreviewUrl(previewUrl);
				if (duration != null && !duration.isEmpty()) this.setDuration(duration);
				if (size != null) this.setSize(size.toString());
			}
			
			this.setEncryption(media.getEncryption() != null && media.getEncryption() ? "true" : "false");
			this.setEncryptionUrl(media.getEncryptionUrl());
			this.setDownloadCount(media.getDownloadCount());
			this.setFreq(media.getFreq());
			this.setAudioPid(media.getAudioPid());
			this.setVideoPid(media.getVideoPid());
			this.setAudioType(media.getAudioType());
			this.setVideoType(media.getVideoType());
			this.setMimsUuid(media.getUuid());
			this.setUpdateTime(new Date());
		}
		return this;
	}
}
