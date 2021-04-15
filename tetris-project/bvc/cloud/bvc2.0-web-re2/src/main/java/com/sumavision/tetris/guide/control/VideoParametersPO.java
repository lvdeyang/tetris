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

	/** 输出组id */
	private Long groupId;

	/** 编码对象 */
	private CodingObject codingObject = CodingObject.H264;
	
	/** 帧率 */
	private String fps = "25";
	
	/** 码率 */
	private Long bitrate = 1500L;
	
	/** 分辨率 */
	private Resolution resolution = Resolution._1920_1080;
	
	/** 宽高比 */
	private Ratio ratio = Ratio._16_9;
	
	/** 码率控制方式 */
	private RcMode rcMode = RcMode.VBR;

	/** 最大码率 */
	private Long maxBitrate = 1500L;

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "CODING_OBJECT")
	@Enumerated(value = EnumType.STRING)
	public CodingObject getCodingObject() {
		return codingObject;
	}

	public void setCodingObject(CodingObject codingObject) {
		this.codingObject = codingObject;
	}

	@Column(name = "FPS")
	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
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
	@Enumerated(EnumType.STRING)
	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}
	
	@Column(name = "RATIO")
	@Enumerated(EnumType.STRING)
	public Ratio getRatio() {
		return ratio;
	}

	public void setRatio(Ratio ratio) {
		this.ratio = ratio;
	}

	
	@Column(name = "RCMODE")
	@Enumerated(EnumType.STRING)
	public RcMode getRcMode() {
		return rcMode;
	}

	public void setRcMode(RcMode rcMode) {
		this.rcMode = rcMode;
	}

	@Column(name = "MAX_BITRATE")
	public Long getMaxBitrate() {
		return maxBitrate;
	}

	public void setMaxBitrate(Long maxBitrate) {
		this.maxBitrate = maxBitrate;
	}
}