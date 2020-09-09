package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import com.sumavision.tetris.cs.menu.CsResourceVO;

public class RequestTerminalScheduleScreenProgramBO {
	/** 播放顺序 */
	private Long index;
	
	private String mediaType;
	
	private String textContent;
	
	private Long duration;
	
	private String fileName;
	
	private String name;
	
	private String freq;
	
	private String audioPid;
	
	private String videoPid;
	
	private String audioType;
	
	private String videoType;

	public Long getIndex() {
		return index;
	}

	public RequestTerminalScheduleScreenProgramBO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public RequestTerminalScheduleScreenProgramBO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getTextContent() {
		return textContent;
	}

	public RequestTerminalScheduleScreenProgramBO setTextContent(String textContent) {
		this.textContent = textContent;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public RequestTerminalScheduleScreenProgramBO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public RequestTerminalScheduleScreenProgramBO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getName() {
		return name;
	}

	public RequestTerminalScheduleScreenProgramBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public RequestTerminalScheduleScreenProgramBO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public RequestTerminalScheduleScreenProgramBO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public RequestTerminalScheduleScreenProgramBO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}

	public String getAudioType() {
		return audioType;
	}

	public RequestTerminalScheduleScreenProgramBO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public String getVideoType() {
		return videoType;
	}

	public RequestTerminalScheduleScreenProgramBO setVideoType(String videoType) {
		this.videoType = videoType;
		return this;
	}
	
	public RequestTerminalScheduleScreenProgramBO setFromResource(CsResourceVO resource) throws Exception {
		this.setName(resource.getName());
		String duration = resource.getDuration();
		if (duration != null && !duration.isEmpty() && !duration.equals("-")) this.setDuration(Long.parseLong(duration));
		if (resource.getType().equals("PUSH_LIVE")) {
			return this.setMediaType("stream")
					.setFreq(resource.getFreq())
					.setAudioPid(resource.getAudioPid())
					.setAudioType(resource.getAudioType())
					.setVideoPid(resource.getVideoPid())
					.setVideoType(resource.getVideoType());
		} else {
			String[] previewUrlSplit = resource.getPreviewUrl().split("/");
			return this.setFileName(resource.getParentPath() + "/" + previewUrlSplit[previewUrlSplit.length - 1])
					.setMediaType(resource.getType().equals("PICTURE") ? "picture" : "file");
		}
	}
}
