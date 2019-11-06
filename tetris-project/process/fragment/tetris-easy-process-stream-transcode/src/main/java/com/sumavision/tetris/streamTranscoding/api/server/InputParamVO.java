package com.sumavision.tetris.streamTranscoding.api.server;

public class InputParamVO {
	private Integer videoPid;
	private Integer audioPid;
	private String videoCodec;
	private String audioCodec;
	private Integer programNum;
	public Integer getVideoPid() {
		return videoPid;
	}
	public void setVideoPid(Integer videoPid) {
		this.videoPid = videoPid;
	}
	public Integer getAudioPid() {
		return audioPid;
	}
	public void setAudioPid(Integer audioPid) {
		this.audioPid = audioPid;
	}
	public String getVideoCodec() {
		return videoCodec;
	}
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}
	public String getAudioCodec() {
		return audioCodec;
	}
	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}
	public Integer getProgramNum() {
		return programNum;
	}
	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}
}
