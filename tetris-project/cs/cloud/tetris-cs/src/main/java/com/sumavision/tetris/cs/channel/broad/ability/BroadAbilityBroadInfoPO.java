package com.sumavision.tetris.cs.channel.broad.ability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_ABILITY_BROAD_INFO")
public class BroadAbilityBroadInfoPO extends AbstractBasePO{

	/**
	 * 能力播发信息（channel频道--页面轮播推流补充信息）
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	/** 预播发地址 */
	private String previewUrlIp;
	/** 预播发可用起始端口 */
	private String previewUrlPort;
	/** 预播发可用终止端口 */
	private String previewUrlEndPort;
	/** 媒资id */
	private Long mediaId;
	/** 预播发用户id */
	private Long userId;
	
	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}
	
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	
	@Column(name = "PREVIEW_URL_IP")
	public String getPreviewUrlIp() {
		return previewUrlIp;
	}
	
	public void setPreviewUrlIp(String previewUrlIp) {
		this.previewUrlIp = previewUrlIp;
	}
	
	@Column(name = "PREVIEW_URL_PORT")
	public String getPreviewUrlPort() {
		return previewUrlPort;
	}
	
	public void setPreviewUrlPort(String previewUrlPort) {
		this.previewUrlPort = previewUrlPort;
	}

	@Column(name = "PREVIEW_URL_END_PORT")
	public String getPreviewUrlEndPort() {
		return previewUrlEndPort;
	}

	public void setPreviewUrlEndPort(String previewUrlEndPort) {
		this.previewUrlEndPort = previewUrlEndPort;
	}

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
