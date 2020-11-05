package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeAudioBO implements EncodeCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 42762904515381014L;
	private String codec;
	private Integer bitrate;
	private Integer sample_rate;
	private Integer sample_fmt;
	private Integer channel;
	private String ch_layout;
	
	public EncodeAudioBO(String codec, Integer bitrate, Integer sample_rate,
			Integer sample_fmt, Integer channel, String ch_layout) {
		super();
		this.codec = codec;
		this.bitrate = bitrate;
		this.sample_rate = sample_rate;
		this.sample_fmt = sample_fmt;
		this.channel = channel;
		this.ch_layout = ch_layout;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public Integer getSample_rate() {
		return sample_rate;
	}
	public void setSample_rate(Integer sample_rate) {
		this.sample_rate = sample_rate;
	}
	public Integer getSample_fmt() {
		return sample_fmt;
	}
	public void setSample_fmt(Integer sample_fmt) {
		this.sample_fmt = sample_fmt;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getCh_layout() {
		return ch_layout;
	}
	public void setCh_layout(String ch_layout) {
		this.ch_layout = ch_layout;
	}
	

}
