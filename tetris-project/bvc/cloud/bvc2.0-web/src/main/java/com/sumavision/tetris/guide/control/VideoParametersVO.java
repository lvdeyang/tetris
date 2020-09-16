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
	
	/** 导播任务id */
	private Long guideId;

	/** 编码对象 */
	private String codingObject;
	
	private String codingObjectName;
	
	/** 编码档次 */
	private String profile;
	
	/** 帧率 */
	private Long fps;
	
	/** 码率 */
	private Long bitrate;
	
	/** 分辨率 */
	private String resolution;
	
	private String resolutionName;
	
	/** 最大码率 */
	private Long maxBitrate;

	public Long getGuideId() {
		return guideId;
	}

	public VideoParametersVO setGuideId(Long guideId) {
		this.guideId = guideId;
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

	public String getProfile() {
		return profile;
	}

	public VideoParametersVO setProfile(String profile) {
		this.profile = profile;
		return this;
	}

	public Long getFps() {
		return fps;
	}

	public VideoParametersVO setFps(Long fps) {
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
		this.setGuideId(entity.getGuideId());
		this.setCodingObject(entity.getCodingObject()!= null ? entity.getCodingObject().toString(): null);
		this.setCodingObjectName(entity.getCodingObject()!= null ? entity.getCodingObject().getName(): null);
		this.setProfile(entity.getProfile());
		this.setFps(entity.getFps());
		this.setBitrate(entity.getBitrate());
		this.setResolution(entity.getResolution()!= null ? entity.getResolution().toString(): null);
		this.setResolutionName(entity.getResolution()!= null ? entity.getResolution().getName(): null);
		this.setMaxBitrate(entity.getMaxBitrate());
		return this;
	}
	
	

}
