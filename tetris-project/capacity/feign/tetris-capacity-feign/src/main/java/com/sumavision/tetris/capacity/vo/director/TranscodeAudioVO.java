package com.sumavision.tetris.capacity.vo.director;

/**
 * 转码音频参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月24日 下午3:05:55
 */
public class TranscodeAudioVO {
	
	/** 音频码率 */
	private Integer bitrate;

	/** 音频编码格式 */
	private String codec;
	
	/** 音频采样率 */
	private Integer sampleRate;

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public Integer getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(Integer sampleRate) {
		this.sampleRate = sampleRate;
	}
	
}
