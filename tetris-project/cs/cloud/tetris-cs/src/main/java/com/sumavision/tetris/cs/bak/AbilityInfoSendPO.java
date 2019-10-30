package com.sumavision.tetris.cs.bak;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_SEND_ABILITY_INFO")
public class AbilityInfoSendPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	
	/** 播发输出ip */
	private String broadUrlIp;
	
	/** 播发输出port */
	private String broadUrlPort;
	
	/** 播发输出是否加密 */
	private Boolean broadEncryption;
	
	/** 视频流媒资id */
	private Long mediaId;
	
	/** 预播发id */
	private Long previewId;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "PREVIEW_ID")
	public Long getPreviewId() {
		return previewId;
	}

	public void setPreviewId(Long previewId) {
		this.previewId = previewId;
	}
	
	@Column(name = "BROAD_URL_IP")
	public String getBroadUrlIp() {
		return broadUrlIp;
	}

	public void setBroadUrlIp(String broadUrlIp) {
		this.broadUrlIp = broadUrlIp;
	}

	@Column(name = "BROAD_URL_PORT")
	public String getBroadUrlPort() {
		return broadUrlPort;
	}

	public void setBroadUrlPort(String broadUrlPort) {
		this.broadUrlPort = broadUrlPort;
	}

	@Column(name = "BROAD_ENCRYPTION")
	public Boolean getBroadEncryption() {
		return broadEncryption;
	}

	public void setBroadEncryption(Boolean broadEncryption) {
		this.broadEncryption = broadEncryption;
	}

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
}
