package com.sumavision.bvc.control.device.group.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.control.system.vo.AvtplGearsVO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAvtplVO extends AbstractBaseVO<DeviceGroupAvtplVO, DeviceGroupAvtplPO>{
	
	/** 参数模板名称 */
	private String name;
	
	/** 视频编码格式（二轮重构多码率不再使用） */
	private String videoFormat;
	
	/** 备用视频编码格式（二轮重构多码率不再使用） */
	private String videoFormatSpare;
	
	/** 音频编码格式（二轮重构多码率不再使用） */
	private String audioFormat;

	private boolean mux;
	
	/** 档位，二轮重构添加 */
	private List<DeviceGroupAvtplGearsVO> gears;

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

	public List<DeviceGroupAvtplGearsVO> getGears() {
		return gears;
	}

	public DeviceGroupAvtplVO setGears(List<DeviceGroupAvtplGearsVO> gears) {
		this.gears = gears;
		return this;
	}

	public boolean isMux() {
		return mux;
	}

	public DeviceGroupAvtplVO setMux(boolean mux) {
		this.mux = mux;
		return this;
	}

	@Override
	public DeviceGroupAvtplVO set(DeviceGroupAvtplPO entity) throws Exception {
		
		this.setId(entity.getId())
			.setName(entity.getName())
			.setVideoFormat(entity.getVideoFormat().getName())
			.setVideoFormatSpare(entity.getVideoFormatSpare().getName())
			.setMux(entity.isMux())
			.setAudioFormat(entity.getAudioFormat().getName());
		if(entity.getGears() != null){
			this.setGears(new ArrayList<DeviceGroupAvtplGearsVO>());
			for(DeviceGroupAvtplGearsPO gear : entity.getGears()){
				this.getGears().add(new DeviceGroupAvtplGearsVO().set(gear));
			}
		}
		return this;
	}	
}
