package com.sumavision.signal.bvc.director.bo;

/**
 * 编码参数--包含音视频<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午2:41:50
 */
public class CodeParamBO {

	private Long videoBitrate;
	
	private String videoCodec;
	
	private String fps;
	
	private String resolution;
	
	private Long audioBitrate;
	
	private String audioCodec;
	
	private Long sample_rate;

	public Long getVideoBitrate() {
		return videoBitrate;
	}

	public void setVideoBitrate(Long videoBitrate) {
		this.videoBitrate = videoBitrate;
	}

	public String getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Long getAudioBitrate() {
		return audioBitrate;
	}

	public void setAudioBitrate(Long audioBitrate) {
		this.audioBitrate = audioBitrate;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	public Long getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(Long sample_rate) {
		this.sample_rate = sample_rate;
	}
	
}
