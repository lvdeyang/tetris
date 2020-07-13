package com.sumavision.tetris.bvc.model;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AudioVideoTemplateVO extends AbstractBaseVO<AudioVideoTemplateVO, AudioVideoTemplatePO>{

	private String name;
	
	private String videoFormat;
	
	private String videoFormatName;
	
	private String videoFormatSpare;
	
	private String videoFormatSpareName;
	
	private String audioFormat;
	
	private String audioFormatName;
	 
	private Boolean mux;
	
	private String videoBitRate;
	
	private String videoBitRateSpare;
	
	private String videoResolution;
	
	private String videoResolutionName;
	
	private String videoResolutionSpare;
	
	private String videoResolutionSpareName;
	
	private String fps;
	
	private String audioBitRate;
	
	private String usageType;
	
	private String usageTypeName;
	
	private Boolean isTemplate;
	
	public String getName() {
		return name;
	}

	public AudioVideoTemplateVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoFormat() {
		return videoFormat;
	}

	public AudioVideoTemplateVO setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}

	public String getVideoFormatName() {
		return videoFormatName;
	}

	public AudioVideoTemplateVO setVideoFormatName(String videoFormatName) {
		this.videoFormatName = videoFormatName;
		return this;
	}

	public String getVideoFormatSpare() {
		return videoFormatSpare;
	}

	public AudioVideoTemplateVO setVideoFormatSpare(String videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
		return this;
	}

	public String getVideoFormatSpareName() {
		return videoFormatSpareName;
	}

	public AudioVideoTemplateVO setVideoFormatSpareName(String videoFormatSpareName) {
		this.videoFormatSpareName = videoFormatSpareName;
		return this;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public AudioVideoTemplateVO setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}

	public String getAudioFormatName() {
		return audioFormatName;
	}

	public AudioVideoTemplateVO setAudioFormatName(String audioFormatName) {
		this.audioFormatName = audioFormatName;
		return this;
	}

	public Boolean getMux() {
		return mux;
	}

	public AudioVideoTemplateVO setMux(Boolean mux) {
		this.mux = mux;
		return this;
	}

	public String getVideoBitRate() {
		return videoBitRate;
	}

	public AudioVideoTemplateVO setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
		return this;
	}

	public String getVideoBitRateSpare() {
		return videoBitRateSpare;
	}

	public AudioVideoTemplateVO setVideoBitRateSpare(String videoBitRateSpare) {
		this.videoBitRateSpare = videoBitRateSpare;
		return this;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public AudioVideoTemplateVO setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
		return this;
	}

	public String getVideoResolutionName() {
		return videoResolutionName;
	}

	public AudioVideoTemplateVO setVideoResolutionName(String videoResolutionName) {
		this.videoResolutionName = videoResolutionName;
		return this;
	}

	public String getVideoResolutionSpare() {
		return videoResolutionSpare;
	}

	public AudioVideoTemplateVO setVideoResolutionSpare(String videoResolutionSpare) {
		this.videoResolutionSpare = videoResolutionSpare;
		return this;
	}

	public String getVideoResolutionSpareName() {
		return videoResolutionSpareName;
	}

	public AudioVideoTemplateVO setVideoResolutionSpareName(String videoResolutionSpareName) {
		this.videoResolutionSpareName = videoResolutionSpareName;
		return this;
	}

	public String getFps() {
		return fps;
	}

	public AudioVideoTemplateVO setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public String getAudioBitRate() {
		return audioBitRate;
	}

	public AudioVideoTemplateVO setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
		return this;
	}

	public String getUsageType() {
		return usageType;
	}

	public AudioVideoTemplateVO setUsageType(String usageType) {
		this.usageType = usageType;
		return this;
	}

	public String getUsageTypeName() {
		return usageTypeName;
	}

	public AudioVideoTemplateVO setUsageTypeName(String usageTypeName) {
		this.usageTypeName = usageTypeName;
		return this;
	}

	public Boolean getIsTemplate() {
		return isTemplate;
	}

	public AudioVideoTemplateVO setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
		return this;
	}

	@Override
	public AudioVideoTemplateVO set(AudioVideoTemplatePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoFormat(entity.getVideoFormat().toString())
			.setVideoFormatName(entity.getVideoFormat().getName())
			.setVideoFormatSpare(entity.getVideoFormatSpare().toString())
			.setVideoFormatSpareName(entity.getVideoFormatSpare().getName())
			.setAudioFormat(entity.getAudioFormat().toString())
			.setAudioFormatName(entity.getAudioFormat().getName())
			.setMux(entity.getMux())
			.setVideoBitRate(entity.getVideoBitRate())
			.setVideoBitRateSpare(entity.getVideoBitRateSpare())
			.setVideoResolution(entity.getVideoResolution().toString())
			.setVideoResolutionName(entity.getVideoResolution().getName())
			.setVideoResolutionSpare(entity.getVideoResolutionSpare().toString())
			.setVideoResolutionSpareName(entity.getVideoResolutionSpare().getName())
			.setFps(entity.getFps())
			.setAudioBitRate(entity.getAudioBitRate())
			.setUsageType(entity.getUsageType().toString())
			.setUsageTypeName(entity.getUsageType().getName())
			.setIsTemplate(entity.getIsTemplate());
		return this;
	}

}
