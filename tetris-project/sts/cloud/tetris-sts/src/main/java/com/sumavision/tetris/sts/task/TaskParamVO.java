package com.sumavision.tetris.sts.task;

import java.util.*;

public class TaskParamVO {

	//多码率情况下的videoPid
	private Integer videoPid;
	
	private Set<VideoParamBO> videoParams = new HashSet<VideoParamBO>();
	
	private Set<AudioParamBO> audioParams = new HashSet<AudioParamBO>();
	
	private ScreenCapBO screenCapBO;
	
	private List<OutputBO> outputs = new ArrayList<OutputBO>();
	
	public Map<Integer,VideoParamBO> getVideoPramMap(){
		Map<Integer,VideoParamBO> map = new HashMap<>();
		videoParams.forEach(video -> {
			map.put(video.getTrackId(),video);
		});
		return map;
	}

	public Map<Integer,AudioParamBO> getAudioPramMap(){
		Map<Integer,AudioParamBO> map = new HashMap<>();
		audioParams.forEach(audio -> {
			map.put(audio.getTrackId(),audio);
		});
		return map;
	}

	public Set<VideoParamBO> getVideoParams() {
		return videoParams;
	}

	public void setVideoParams(Set<VideoParamBO> videoParams) {
		this.videoParams = videoParams;
	}

	public Set<AudioParamBO> getAudioParams() {
		return audioParams;
	}

	public void setAudioParams(Set<AudioParamBO> audioParams) {
		this.audioParams = audioParams;
	}


	public ScreenCapBO getScreenCapBO() {
		return screenCapBO;
	}

	public void setScreenCapBO(ScreenCapBO screenCapBO) {
		this.screenCapBO = screenCapBO;
	}

	public List<OutputBO> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<OutputBO> outputs) {
		this.outputs = outputs;
	}

	public Integer getVideoPid() {
		return videoPid;
	}

	public void setVideoPid(Integer videoPid) {
		this.videoPid = videoPid;
	}
}

