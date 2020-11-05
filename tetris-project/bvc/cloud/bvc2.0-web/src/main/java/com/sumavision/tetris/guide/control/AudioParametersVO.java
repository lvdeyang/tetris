/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月15日 下午1:13:01
 */
public class AudioParametersVO extends AbstractBaseVO<AudioParametersVO, AudioParametersPO>{
	
	/** 输出组id */
	private Long groupId;

	/** 编码格式 */
	private String codingFormat;
	
	private String codingFormatName;
	
	/** 声道布局 */
	private String channelLayout;
	
	private String channelLayoutName;
	
	/** 码率 */
	private String bitrate;
	
	/** 采样率 */
	private String sampleRate;
	
	/** 编码类型 */
	private String codingType;
	
	private String codingTypeName;

	public Long getGroupId() {
		return groupId;
	}

	public AudioParametersVO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getCodingFormat() {
		return codingFormat;
	}

	public AudioParametersVO setCodingFormat(String codingFormat) {
		this.codingFormat = codingFormat;
		return this;
	}

	public String getCodingFormatName() {
		return codingFormatName;
	}

	public AudioParametersVO setCodingFormatName(String codingFormatName) {
		this.codingFormatName = codingFormatName;
		return this;
	}
	
	public String getChannelLayout() {
		return channelLayout;
	}

	public AudioParametersVO setChannelLayout(String channelLayout) {
		this.channelLayout = channelLayout;
		return this;
	}

	public String getChannelLayoutName() {
		return channelLayoutName;
	}

	public AudioParametersVO setChannelLayoutName(String channelLayoutName) {
		this.channelLayoutName = channelLayoutName;
		return this;
	}

	public String getSampleRate() {
		return sampleRate;
	}

	public AudioParametersVO setSampleRate(String sampleRate) {
		this.sampleRate = sampleRate;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public AudioParametersVO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public String getCodingType() {
		return codingType;
	}

	public AudioParametersVO setCodingType(String codingType) {
		this.codingType = codingType;
		return this;
	}

	public String getCodingTypeName() {
		return codingTypeName;
	}

	public AudioParametersVO setCodingTypeName(String codingTypeName) {
		this.codingTypeName = codingTypeName;
		return this;
	}


	@Override
	public AudioParametersVO set(AudioParametersPO entity) throws Exception {
		this.setGroupId(entity.getGroupId());
		this.setId(entity.getId());
		this.setCodingFormat(entity.getCodingFormat()!= null ? entity.getCodingFormat().toString(): null);
		this.setCodingFormatName(entity.getCodingFormat()!= null ? entity.getCodingFormat().getName(): null);
		this.setChannelLayout(entity.getChannelLayout()!= null ? entity.getChannelLayout().toString(): null);
		this.setChannelLayoutName(entity.getChannelLayout()!= null ? entity.getChannelLayout().getName(): null);
		this.setBitrate(entity.getBitrate());
		this.setSampleRate(entity.getSampleRate());
		this.setCodingType(entity.getCodingType()!= null ? entity.getCodingType().toString(): null);
		this.setCodingTypeName(entity.getCodingType()!= null ? entity.getCodingType().getName(): null);
		return this;
	}
	
	
}
