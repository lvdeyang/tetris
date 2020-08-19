package com.sumavision.tetris.cs.channel.broad.ability.transcode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_BROAD_TRANSCODE_OUTPUT_PERMISSION")
public class BroadTranscodeOutputPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	
	/** 输出地址 */
	private String outputUrl;
	
	/** 能力ip */
	private String localIp;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "OUTPUT_URL")
	public String getOutputUrl() {
		return outputUrl;
	}

	public void setOutputUrl(String outputUrl) {
		this.outputUrl = outputUrl;
	}

	@Column(name = "LOCAL_IP")
	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
}
