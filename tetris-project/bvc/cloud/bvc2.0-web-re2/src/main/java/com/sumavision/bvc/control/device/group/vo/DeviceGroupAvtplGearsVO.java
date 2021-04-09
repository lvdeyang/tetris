package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAvtplGearsVO extends AbstractBaseVO<DeviceGroupAvtplGearsVO, DeviceGroupAvtplGearsPO> {
	
	/** 档位名称 */
	private String name;
	
	/** 视频码率：一般设备的通道1用这个码率 */
	private String videoBitRate;
	
	/** 备用视频码率：一般设备除通道1以外的通道用这个码率（二轮重构多码率不再使用） */
	private String videoBitRateSpare;
	
	/** 视频分辨率：一般设备的通道1用这个分辨率 */
	private String videoResolution;
	
	/** 备用视频分辨率：一般设备除通道1以外的通道用这个分辨率（二轮重构多码率不再使用） */
	private String videoResolutionSpare;
	
	/** 视频编码格式 */
	private String videoFormat;
	
	/** 音频编码格式 */
	private String audioFormat;
	
	/** 音频码率 */
	private String audioBitRate;
	
	/** 档位 */
	private String level;

	/** 视频帧率 */
	private String frameRate;

	private String channelParamsType;

	public DeviceGroupAvtplGearsVO setChannelParamsType(String channelParamsType) {
		this.channelParamsType = channelParamsType;
		return this;
	}

	public String getChannelParamsType() {
		return channelParamsType;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupAvtplGearsVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoBitRate() {
		return videoBitRate;
	}

	public DeviceGroupAvtplGearsVO setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
		return this;
	}

	public String getVideoBitRateSpare() {
		return videoBitRateSpare;
	}

	public DeviceGroupAvtplGearsVO setVideoBitRateSpare(String videoBitRateSpare) {
		this.videoBitRateSpare = videoBitRateSpare;
		return this;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public DeviceGroupAvtplGearsVO setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
		return this;
	}

	public String getVideoResolutionSpare() {
		return videoResolutionSpare;
	}

	public DeviceGroupAvtplGearsVO setVideoResolutionSpare(String videoResolutionSpare) {
		this.videoResolutionSpare = videoResolutionSpare;
		return this;
	}

	public String getVideoFormat() {
		return videoFormat;
	}

	public DeviceGroupAvtplGearsVO setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public DeviceGroupAvtplGearsVO setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}

	public String getAudioBitRate() {
		return audioBitRate;
	}

	public DeviceGroupAvtplGearsVO setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
		return this;
	}

	public String getLevel() {
		return level;
	}

	public DeviceGroupAvtplGearsVO setLevel(String level) {
		this.level = level;
		return this;
	}

	public String getFrameRate() {
		return frameRate;
	}

	public DeviceGroupAvtplGearsVO setFrameRate(String frameRate) {
		this.frameRate = frameRate;
		return this;
	}

	@Override
	public DeviceGroupAvtplGearsVO set(DeviceGroupAvtplGearsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoBitRate(entity.getVideoBitRate())
			.setVideoResolution(entity.getVideoResolution().getName())
			.setVideoBitRateSpare(entity.getVideoBitRateSpare())
			.setVideoResolutionSpare(entity.getVideoResolutionSpare().getName())
			.setVideoFormat(entity.getVideoFormat()==null?null:entity.getVideoFormat().getName())
			.setAudioFormat(entity.getAudioFormat()==null?null:entity.getAudioFormat().getName())
			.setAudioBitRate(entity.getAudioBitRate())
			.setFrameRate(entity.getFps())
			.setChannelParamsType(entity.getChannelParamsType()==null?"":entity.getChannelParamsType().getName())
			.setLevel(entity.getLevel()==null?"":entity.getLevel().getName());
		
		return this;
	}
	
	
}
