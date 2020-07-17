package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;

import com.sumavision.tetris.orm.po.AbstractBasePO;

public class Jv230CombineAudioSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 源接入层 */
	private String sourceLayerId;
	
	/** 源设备id */
	private String sourceBundleId;
	
	/** 源音频通道 */
	private String sourceChannelId;
	
	/** 分屏序号 */
	private int serialNum;
	
	/** 隶属混音 */
	private Long jv230CombineAudioId;

	@Column(name = "SOURCE_LAYER_ID")
	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	@Column(name = "SOURCE_BUNDLE_ID")
	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public void setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
	}

	@Column(name = "SOURCE_CHANNEL_ID")
	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public void setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
	}

	@Column(name = "SERIAL_NUM")
	public int getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	@Column(name = "JV230_COMBINE_AUDIO_ID")
	public Long getJv230CombineAudioId() {
		return jv230CombineAudioId;
	}

	public void setJv230CombineAudioId(Long jv230CombineAudioId) {
		this.jv230CombineAudioId = jv230CombineAudioId;
	}
	
}
