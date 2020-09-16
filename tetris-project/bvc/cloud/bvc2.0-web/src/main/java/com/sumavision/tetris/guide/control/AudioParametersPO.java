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
	
	/** 导播任务id */
	private Long guideId;

	/** 编码格式 */
	private CodingFormat codingFormat = CodingFormat.ACC;
	
	/** 采样率 */
	private String sampleFmt = "s16";
	
	/** 码率 */
	private String bitrate = "128";
	
	/** 编码类型 */
	private CodingType codingType = CodingType.MPEG4_ACC_LC;
	
	@Column(name = "GUIDE_ID")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@Column(name = "CODING_FORMAT")
	@Enumerated(EnumType.STRING)
	public CodingFormat getCodingFormat() {
		return codingFormat;
	}

	public void setCodingFormat(CodingFormat codingFormat) {
		this.codingFormat = codingFormat;
	}

	@Column(name = "SAMPLE_FMT")
	public String getSampleFmt() {
		return sampleFmt;
	}

	public void setSampleFmt(String sampleFmt) {
		this.sampleFmt = sampleFmt;
	}

	@Column(name = "BITRATE")
	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	@Column(name = "CODING_TYPE")
	@Enumerated(EnumType.STRING)
	public CodingType getCodingType() {
		return codingType;
	}

	public void setCodingType(CodingType codingType) {
		this.codingType = codingType;
	}
	
	
}
