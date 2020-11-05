package com.sumavision.tetris.transcoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlElement;

public class TargetVO {
	private String transcodeTargetParams;
	private String targetURI;
	private String videoTitle;
	private String videoFrom;
	private String videoType;
	private Integer hsdFlag;

	public String getTranscodeTargetParams() {
		return transcodeTargetParams;
	}

	@XmlElement(name = "TranscodeTargetParams")
	public void setTranscodeTargetParams(String transcodeTargetParams) {
		this.transcodeTargetParams = transcodeTargetParams;
	}

	public String getTargetURI() {
		return targetURI;
	}

	@XmlElement(name = "TargetURI")
	public void setTargetURI(String targetURI) {
		this.targetURI = targetURI;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	@XmlElement(name = "VideoTitle")
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getVideoFrom() {
		return videoFrom;
	}

	@XmlElement(name = "VideoFrom")
	public void setVideoFrom(String videoFrom) {
		this.videoFrom = videoFrom;
	}

	public String getVideoType() {
		return videoType;
	}

	@XmlElement(name = "VideoType")
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public Integer getHsdFlag() {
		return hsdFlag;
	}

	@XmlElement(name = "HsdFlag")
	public void setHsdFlag(Integer hsdFlag) {
		this.hsdFlag = hsdFlag;
	}
}
