package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeAudioG711aBO implements EncodeAudioCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1385406120741996597L;
	private Integer bitrate;
	private Integer sample_rate;
	private Integer sample_byte;
	//private Integer channel;
	private String channel_layout;
	private Integer ptime;
	
	public EncodeAudioG711aBO(){
		super();
	}
	public EncodeAudioG711aBO(Integer bitrate, Integer sample_rate,
			Integer sample_byte, String channel_layout,
			Integer ptime) {
		super();
		this.bitrate = bitrate*1000;
		this.sample_rate = sample_rate;
		this.sample_byte = sample_byte;
		this.channel_layout = channel_layout;
		this.ptime = ptime;
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
	public String getChannel_layout() {
		return channel_layout;
	}
	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}
	public Integer getPtime() {
		return ptime;
	}
	public void setPtime(Integer ptime) {
		this.ptime = ptime;
	}
	
	

}
