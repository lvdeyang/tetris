package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class OutputMediaGroupBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6800098005499161027L;
	private Integer bandwidth;
	private Integer video;
	private ArrayList<AudioIndexBO> audio;
	private ArrayList<SubtitleIndexBO> subtitle;
	public Integer getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}
	public Integer getVideo() {
		return video;
	}
	public void setVideo(Integer video) {
		this.video = video;
	}
	public ArrayList<AudioIndexBO> getAudio() {
		return audio;
	}
	public void setAudio(ArrayList<AudioIndexBO> audio) {
		this.audio = audio;
	}
	public ArrayList<SubtitleIndexBO> getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(ArrayList<SubtitleIndexBO> subtitle) {
		this.subtitle = subtitle;
	}
	

}
