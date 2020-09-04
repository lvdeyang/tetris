/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月2日 下午6:59:51
 */
@Entity
@Table(name = "TETRIS_VIDEO_PARAMTERS_PO")
public class VideoParametersPO extends AbstractBasePO{
	
	/** 视频参数编号 */
	private Long parameterNumber;
	
	/** 视频格式 */
	private String format;
	
	/** 视频库 */
	private String codingLibrary;
	
	/** 编码级别 */
	private Long codingLevel;
	
	/** 编码档次 */
	private String codingGrade;
	
	/** 码率控制方式 */
	private String rateControlMode;
	
	/** 分辨率 */
	private String resolvingPower;
	
	/** 分辨率宽高 */
	private String resolutionWidthAndHeight;
	
	/** 宽高比 */
	private String aspectRatio;
	
	/** 帧率 */
	private Long frameRate;
	
	/** 码率 */
	private Long bitRate;
	
	@Column(name = "PARAMETER_NUMBER")
	public Long getParameterNumber() {
		return parameterNumber;
	}

	public void setParameterNumber(Long parameterNumber) {
		this.parameterNumber = parameterNumber;
	}
	
	@Column(name = "FORMAT")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	@Column(name = "CODING_LIBRARY")
	public String getCodingLibrary() {
		return codingLibrary;
	}

	public void setCodingLibrary(String codingLibrary) {
		this.codingLibrary = codingLibrary;
	}
	
	@Column(name = "CODING_LEVEL")
	public Long getCodingLevel() {
		return codingLevel;
	}

	public void setCodingLevel(Long codingLevel) {
		this.codingLevel = codingLevel;
	}
	
	@Column(name = "CODING_GRADE")
	public String getCodingGrade() {
		return codingGrade;
	}

	public void setCodingGrade(String codingGrade) {
		this.codingGrade = codingGrade;
	}
	
	@Column(name = "RATE_CONTROL_MODE")
	public String getRateControlMode() {
		return rateControlMode;
	}

	public void setRateControlMode(String rateControlMode) {
		this.rateControlMode = rateControlMode;
	}
	
	
	@Column(name = "RESOLVING_POWER")
	public String getResolvingPower() {
		return resolvingPower;
	}

	public void setResolvingPower(String resolvingPower) {
		this.resolvingPower = resolvingPower;
	}
	
	@Column(name = "RESOLUTION_WIDTH_AND_HEIGHT")
	public String getResolutionWidthAndHeight() {
		return resolutionWidthAndHeight;
	}

	public void setResolutionWidthAndHeight(String resolutionWidthAndHeight) {
		this.resolutionWidthAndHeight = resolutionWidthAndHeight;
	}
	
	@Column(name = "ASPECT_RATIO")
	public String getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
	
	@Column(name = "FRAME_RATE")
	public Long getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(Long frameRate) {
		this.frameRate = frameRate;
	}
	
	@Column(name = "BIT_RATE")
	public Long getBitRate() {
		return bitRate;
	}

	public void setBitRate(Long bitRate) {
		this.bitRate = bitRate;
	}

}
