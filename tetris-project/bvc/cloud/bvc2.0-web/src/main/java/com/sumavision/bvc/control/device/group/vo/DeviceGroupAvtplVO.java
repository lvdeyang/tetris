package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAvtplVO extends AbstractBaseVO<DeviceGroupAvtplVO, DeviceGroupAvtplPO>{
	
	/** 参数模板名称 */
	private String name;
	
	/** 视频编码格式 */
	private String videoFormat;
	
	/** 备用视频编码格式 */
	private String videoFormatSpare;
	
	/** 音频编码格式 */
	private String audioFormat;

	public String getName() {
		return name;
	}

	public DeviceGroupAvtplVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoFormat() {
		return videoFormat;
	}

	public DeviceGroupAvtplVO setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}

	public String getVideoFormatSpare() {
		return videoFormatSpare;
	}

	public DeviceGroupAvtplVO setVideoFormatSpare(String videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
		return this;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public DeviceGroupAvtplVO setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}

	@Override
	public DeviceGroupAvtplVO set(DeviceGroupAvtplPO entity) throws Exception {
		
		this.setId(entity.getId())
			.setName(entity.getName())
			.setVideoFormat(entity.getVideoFormat().getName())
			.setVideoFormatSpare(entity.getVideoFormatSpare().getName())
			.setAudioFormat(entity.getAudioFormat().getName());
		
		return this;
	}	
}
