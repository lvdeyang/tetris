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
	
	/** 导播任务id */
	private Long guideId;

	/** 编码格式 */
	private String codingFormat;
	
	private String codingFormatName;
	
	/** 采样率 */
	private String sampleFmt;
	
	/** 码率 */
	private String bitrate;
	
	/** 编码类型 */
	private String codingType;
	
	private String codingTypeName;

	public Long getGuideId() {
		return guideId;
	}

	public AudioParametersVO setGuideId(Long guideId) {
		this.guideId = guideId;
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

	public String getSampleFmt() {
		return sampleFmt;
	}

	public AudioParametersVO setSampleFmt(String sampleFmt) {
		this.sampleFmt = sampleFmt;
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
		this.setGuideId(entity.getGuideId());
		this.setCodingFormat(entity.getCodingFormat()!= null ? entity.getCodingFormat().toString(): null);
		this.setCodingFormatName(entity.getCodingFormat()!= null ? entity.getCodingFormat().getName(): null);
		this.setSampleFmt(entity.getSampleFmt());
		this.setBitrate(entity.getBitrate());
		this.setCodingType(entity.getCodingType()!= null ? entity.getCodingType().toString(): null);
		this.setCodingTypeName(entity.getCodingType()!= null ? entity.getCodingType().getName(): null);
		return this;
	}
	
	
}
