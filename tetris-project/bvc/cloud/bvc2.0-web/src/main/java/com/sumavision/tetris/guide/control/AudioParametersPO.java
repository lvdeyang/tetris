/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月10日 下午2:27:21
 */
@Entity
@Table(name = "TETRIS_AUDIO_PARAMETERS_PO")
public class AudioParametersPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 输出组id */
	private Long groupId;

	/** 编码格式 */
	private CodingFormat codingFormat = CodingFormat.AAC;
	
	/** 声道布局 */
	private ChannelLayout channelLayout = ChannelLayout.MONO;
	
	/** 码率 */
	private String bitrate = "128";
	
	/** 采样率 */
	private String sampleRate = "44.1";
	
	/** 编码类型 */
	private CodingType codingType = CodingType.MPEG4_ACC_LC;
	
	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "CODING_FORMAT")
	@Enumerated(value = EnumType.STRING)
	public CodingFormat getCodingFormat() {
		return codingFormat;
	}

	public void setCodingFormat(CodingFormat codingFormat) {
		this.codingFormat = codingFormat;
	}

	@Column(name = "CHANNEL_LAYOUT")
	@Enumerated(value = EnumType.STRING)
	public ChannelLayout getChannelLayout() {
		return channelLayout;
	}

	public void setChannelLayout(ChannelLayout channelLayout) {
		this.channelLayout = channelLayout;
	}

	@Column(name = "SAMPLE_RATE")
	public String getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(String sampleRate) {
		this.sampleRate = sampleRate;
	}

	@Column(name = "BITRATE")
	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	@Column(name = "CODING_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CodingType getCodingType() {
		return codingType;
	}

	public void setCodingType(CodingType codingType) {
		this.codingType = codingType;
	}
	
	
}
