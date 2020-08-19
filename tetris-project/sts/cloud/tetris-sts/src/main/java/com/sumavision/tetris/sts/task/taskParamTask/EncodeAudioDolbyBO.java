package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeAudioDolbyBO implements EncodeAudioCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3534328534262106754L;
	private Integer bitrate;
	private Integer sample_rate;
	private Integer sample_byte;
	//private Integer channel;
	private String sample_fmt;
	private String channel_layout;
	private String type;
	
	public EncodeAudioDolbyBO(){
		super();
	}
	public EncodeAudioDolbyBO(Integer bitrate, Integer sample_rate,
			Integer sample_byte, String sample_fmt,
			String channel_layout, String type) {
		super();
		this.bitrate = bitrate*1000;
		this.sample_rate = sample_rate;
		this.sample_byte = sample_byte;
		this.sample_fmt = sample_fmt;
		this.channel_layout = channel_layout;
		this.type = type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
