package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeAudioMp2BO implements EncodeAudioCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6266986268579929839L;

	private Integer bitrate;
	private Integer sample_rate;
	private Integer sample_byte;
	private String sample_fmt;
	//private Integer channel;
	private String channel_layout;
	
	public EncodeAudioMp2BO(){
		super();
	}
	
	public EncodeAudioMp2BO(Integer bitrate, Integer sample_rate,
			Integer sample_byte, String sample_fmt, String channel_layout) {
		super();
		this.bitrate = bitrate*1000;
		this.sample_rate = sample_rate;
		this.sample_byte = sample_byte;
		this.sample_fmt = sample_fmt;
		this.channel_layout = channel_layout;
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

	public Integer getSample_byte() {
		return sample_byte;
	}

	public void setSample_byte(Integer sample_byte) {
		this.sample_byte = sample_byte;
	}

	public String getSample_fmt() {
		return sample_fmt;
	}

	public void setSample_fmt(String sample_fmt) {
		this.sample_fmt = sample_fmt;
	}

	public String getChannel_layout() {
		return channel_layout;
	}

	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}
	
	
}
