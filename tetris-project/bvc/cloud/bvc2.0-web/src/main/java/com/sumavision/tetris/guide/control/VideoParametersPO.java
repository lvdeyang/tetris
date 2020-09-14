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

	private static final long serialVersionUID = 1L;

	/** 导播任务id */
	private Long guideId;

	/** 编码对象 */
	private CodingObject codingObject = CodingObject.H264;
	
	/** 编码档次 */
	private String profile = "main";
	
	/** 帧率 */
	private Long fps = 25L;
	
	/** 码率 */
	private Long bitrate = 1500l;
	
	/** 分辨率 */
	private String resolution = "1920x1080";
	
	/** 最大码率 */
	private Long maxBitrate = 1500L;
	
	@Column(name = "GUIDE_ID")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@Column(name = "CODING_OBJECT")
	@Enumerated(value = EnumType.STRING)
	public CodingObject getCodingObject() {
		return codingObject;
	}

	public void setCodingObject(CodingObject codingObject) {
		this.codingObject = codingObject;
	}

	@Column(name = "PROFILE")
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Column(name = "FPS")
	public Long getFps() {
		return fps;
	}

	public void setFps(Long fps) {
		this.fps = fps;
	}

	@Column(name = "BITRATE")
	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	@Column(name = "RESOLUTION")
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	@Column(name = "MAX_BITRATE")
	public Long getMaxBitrate() {
		return maxBitrate;
	}

	public void setMaxBitrate(Long maxBitrate) {
		this.maxBitrate = maxBitrate;
	}
}
