package com.sumavision.bvc.control.device.monitor.device;

import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CodecParamVO extends AbstractBaseVO<CodecParamVO, Object>{

	private Long gearId;
	
	private String name;
	
	private String videoFormat;
	
	private String audioFormat;
	
	private String videoBitRate;
	
	private String videoResolution;
	
	private String audioBitRate;
	
	public Long getGearId() {
		return gearId;
	}

	public CodecParamVO setGearId(Long gearId) {
		this.gearId = gearId;
		return this;
	}

	public String getName() {
		return name;
	}

	public CodecParamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoFormat() {
		return videoFormat;
	}

	public CodecParamVO setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public CodecParamVO setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}

	public String getVideoBitRate() {
		return videoBitRate;
	}

	public CodecParamVO setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
		return this;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public CodecParamVO setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
		return this;
	}

	public String getAudioBitRate() {
		return audioBitRate;
	}

	public CodecParamVO setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
		return this;
	}

	@Override
	public CodecParamVO set(Object entity) throws Exception {
		return this;
	}
	
	public CodecParamVO set(AvtplPO tpl, AvtplGearsPO gear){
		this.setId(tpl.getId())
			.setGearId(gear.getId())
			.setName(new StringBufferWrapper().append(tpl.getName()).append("-").append(gear.getName()).toString())
			.setVideoFormat(tpl.getVideoFormat().getName())
			.setAudioFormat(tpl.getAudioFormat().getName())
			.setVideoBitRate(gear.getVideoBitRate())
			.setVideoResolution(gear.getVideoResolution().getName())
			.setAudioBitRate(gear.getAudioBitRate());
		return this;
	}
}
