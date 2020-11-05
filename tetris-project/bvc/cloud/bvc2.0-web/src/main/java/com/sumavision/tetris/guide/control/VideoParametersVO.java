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
 * <b>日期：</b>2020年9月15日 上午11:29:01
 */
public class VideoParametersVO extends AbstractBaseVO<VideoParametersVO, VideoParametersPO>{
	
	/** 输出组id */
	private Long groupId;

	/** 编码对象 */
	private String codingObject;
	
	private String codingObjectName;
	
	/** 帧率 */
	private String fps;
	
	/** 码率 */
	private Long bitrate;
	
	/** 分辨率 */
	private String resolution;
	
	private String resolutionName;
	
	/** 宽高比 */
	private String ratio;
	
	private String ratioName;
	
	/** 码率控制方式 */
	private String rcMode;
	
	private String rcModeName;
	
	/** 最大码率 */
	private Long maxBitrate;

	public String getRatio() {
		return ratio;
	}

	public VideoParametersVO setRatio(String ratio) {
		this.ratio = ratio;
		return this;
	}

	public String getRatioName() {
		return ratioName;
	}

	public VideoParametersVO setRatioName(String ratioName) {
		this.ratioName = ratioName;
		return this;
	}

	public String getRcMode() {
		return rcMode;
	}

	public VideoParametersVO setRcMode(String rcMode) {
		this.rcMode = rcMode;
		return this;
	}

	public String getRcModeName() {
		return rcModeName;
	}

	public VideoParametersVO setRcModeName(String rcModeName) {
		this.rcModeName = rcModeName;
		return this;
	}

	public Long getGroupId() {
		return groupId;
	}

	public VideoParametersVO setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getCodingObject() {
		return codingObject;
	}

	public VideoParametersVO setCodingObject(String codingObject) {
		this.codingObject = codingObject;
		return this;
	}

	public String getCodingObjectName() {
		return codingObjectName;
	}

	public VideoParametersVO setCodingObjectName(String codingObjectName) {
		this.codingObjectName = codingObjectName;
		return this;
	}

	public String getFps() {
		return fps;
	}

	public VideoParametersVO setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public VideoParametersVO setBitrate(Long bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public String getResolution() {
		return resolution;
	}

	public VideoParametersVO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}

	public String getResolutionName() {
		return resolutionName;
	}

	public VideoParametersVO setResolutionName(String resolutionName) {
		this.resolutionName = resolutionName;
		return this;
	}

	public Long getMaxBitrate() {
		return maxBitrate;
	}

	public VideoParametersVO setMaxBitrate(Long maxBitrate) {
		this.maxBitrate = maxBitrate;
		return this;
	}

	@Override
	public VideoParametersVO set(VideoParametersPO entity) throws Exception {
		this.setGroupId(entity.getGroupId());
		this.setId(entity.getId());
		this.setCodingObject(entity.getCodingObject()!= null ? entity.getCodingObject().toString(): null);
		this.setCodingObjectName(entity.getCodingObject()!= null ? entity.getCodingObject().getName(): null);
		this.setFps(entity.getFps());
		this.setBitrate(entity.getBitrate());
		this.setResolution(entity.getResolution()!= null ? entity.getResolution().toString(): null);
		this.setResolutionName(entity.getResolution()!= null ? entity.getResolution().getName(): null);
		this.setRatio(entity.getRatio()!= null ? entity.getRatio().toString(): null);
		this.setRatioName(entity.getRatio()!= null ? entity.getRatio().getName(): null);
		this.setRcMode(entity.getRcMode()!= null ? entity.getRcMode().toString(): null);
		this.setRcModeName(entity.getRcMode()!= null ? entity.getRcMode().getName(): null);
		this.setMaxBitrate(entity.getMaxBitrate());
		return this;
	}
	
	

}
