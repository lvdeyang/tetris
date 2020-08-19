package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class AudioProcessBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8050052347741083738L;
	private Integer sample;
	private String codec;
	private Integer volume;
	private String ch_layout;
	private String denoise;
	private String audio_dup_mode;
	private Integer sample_fmt;
	private String agc_gain;
	
	public AudioProcessBO(Integer sample, String codec, Integer volume,
			String ch_layout, String denoise, String audio_dup_mode,
			Integer sample_fmt, String agc_gain) {
		super();
		this.sample = sample;
		this.codec = codec;
		this.volume = volume;
		this.ch_layout = ch_layout;
		this.denoise = denoise;
		this.audio_dup_mode = audio_dup_mode;
		this.sample_fmt = sample_fmt;
		this.agc_gain = agc_gain;
	}
	public Integer getSample() {
		return sample;
	}
	public void setSample(Integer sample) {
		this.sample = sample;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public String getCh_layout() {
		return ch_layout;
	}
	public void setCh_layout(String ch_layout) {
		this.ch_layout = ch_layout;
	}
	public String getDenoise() {
		return denoise;
	}
	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}
	public String getAudio_dup_mode() {
		return audio_dup_mode;
	}
	public void setAudio_dup_mode(String audio_dup_mode) {
		this.audio_dup_mode = audio_dup_mode;
	}
	public Integer getSample_fmt() {
		return sample_fmt;
	}
	public void setSample_fmt(Integer sample_fmt) {
		this.sample_fmt = sample_fmt;
	}
	public String getAgc_gain() {
		return agc_gain;
	}
	public void setAgc_gain(String agc_gain) {
		this.agc_gain = agc_gain;
	}
	
	
}
