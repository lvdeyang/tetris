package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class AudioResampleBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7776701762483238619L;
	private Integer sample_rate;
	//private Integer channels;
	private String channel_layout;
	private String format;
	public AudioResampleBO(Integer sample_rate,String channel_layout,String format) {
		super();
		this.sample_rate = sample_rate;
		this.channel_layout = channel_layout;
		this.format = format;
	}
	
	public Integer getSample_rate() {
		return sample_rate;
	}

	public void setSample_rate(Integer sample_rate) {
		this.sample_rate = sample_rate;
	}

	public String getChannel_layout() {
		return channel_layout;
	}
	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	

}
