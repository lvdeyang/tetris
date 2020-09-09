package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AvtplVO extends AbstractBaseVO<AvtplVO, AvtplPO>{
	
	private String name;
	
	private String videoFormat;
	
	private String videoFormatSpare;
	
	private String audioFormat;
	
	private String usageType;
	
	private boolean mux;
	
	public String getName() {
		return name;
	}
	
	public AvtplVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getVideoFormat() {
		return videoFormat;
	}
	
	public AvtplVO setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}
	
	public String getVideoFormatSpare() {
		return videoFormatSpare;
	}
	
	public AvtplVO setVideoFormatSpare(String videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
		return this;
	}
	
	public String getAudioFormat() {
		return audioFormat;
	}

	public AvtplVO setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}
	
	public String getUsageType() {
		return usageType;
	}

	public AvtplVO setUsageType(String usageType) {
		this.usageType = usageType;
		return this;
	}

	public boolean isMux() {
		return mux;
	}

	public AvtplVO setMux(boolean mux) {
		this.mux = mux;
		return this;
	}

	@Override
	public AvtplVO set(AvtplPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAudioFormat(entity.getAudioFormat()==null?"":entity.getAudioFormat().getName())
			.setVideoFormat(entity.getVideoFormat()==null?"":entity.getVideoFormat().getName())
			.setVideoFormatSpare(entity.getVideoFormatSpare()==null?"":entity.getVideoFormatSpare().getName())
			.setUsageType(entity.getUsageType()==null?"":entity.getUsageType().getName())
			.setMux(entity.getMux()==null?false:entity.getMux());
		return this;
	}
	
}
