package com.sumavision.bvc.control.system.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AvtplVO extends AbstractBaseVO<AvtplVO, AvtplPO>{
	
	private String name;
	
	private String videoFormat;//（二轮重构多码率不再使用）
	
	private String videoFormatSpare;//（二轮重构多码率不再使用）
	
	private String audioFormat;//（二轮重构多码率不再使用）
	
	private String usageType;
	
	private boolean mux;
	
	/** 档位，二轮重构添加 */
	private List<AvtplGearsVO> gears;
	
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

	public List<AvtplGearsVO> getGears() {
		return gears;
	}

	public AvtplVO setGears(List<AvtplGearsVO> gears) {
		this.gears = gears;
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
		if(entity.getGears() != null){
			this.setGears(new ArrayList<AvtplGearsVO>());
			for(AvtplGearsPO gear : entity.getGears()){
				this.getGears().add(new AvtplGearsVO().set(gear));
			}
		}
		return this;
	}
	
}
