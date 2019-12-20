package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class PreAudioBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5256716186242348417L;
	
	private Integer sample;
	private String codec;
	private Integer volume;
	private Integer channel;
	private String denoise;
	private String audio_dup_mode;
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
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
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
	

}
