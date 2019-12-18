package com.sumavision.tetris.sts.task.taskParamOutput;

import java.util.ArrayList;

public class OutputMediaEncodeMessage {
	private ArrayList<String> videoEncodeId;
	private ArrayList<String> videoTaskId;
	
	private ArrayList<String> audioEncodeId;
	private ArrayList<String> audioTaskId;
	
	private ArrayList<String> subtitleEncodeId;
	private ArrayList<String> subtitleTaskId;
	public ArrayList<String> getVideoEncodeId() {
		return videoEncodeId;
	}
	public void setVideoEncodeId(ArrayList<String> videoEncodeId) {
		this.videoEncodeId = videoEncodeId;
	}
	public ArrayList<String> getVideoTaskId() {
		return videoTaskId;
	}
	public void setVideoTaskId(ArrayList<String> videoTaskId) {
		this.videoTaskId = videoTaskId;
	}
	public ArrayList<String> getAudioEncodeId() {
		return audioEncodeId;
	}
	public void setAudioEncodeId(ArrayList<String> audioEncodeId) {
		this.audioEncodeId = audioEncodeId;
	}
	public ArrayList<String> getAudioTaskId() {
		return audioTaskId;
	}
	public void setAudioTaskId(ArrayList<String> audioTaskId) {
		this.audioTaskId = audioTaskId;
	}
	public ArrayList<String> getSubtitleEncodeId() {
		return subtitleEncodeId;
	}
	public void setSubtitleEncodeId(ArrayList<String> subtitleEncodeId) {
		this.subtitleEncodeId = subtitleEncodeId;
	}
	public ArrayList<String> getSubtitleTaskId() {
		return subtitleTaskId;
	}
	public void setSubtitleTaskId(ArrayList<String> subtitleTaskId) {
		this.subtitleTaskId = subtitleTaskId;
	}
	

}
