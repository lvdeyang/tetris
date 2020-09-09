package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAvtplGearsVO extends AbstractBaseVO<DeviceGroupAvtplGearsVO, DeviceGroupAvtplGearsPO> {

	private static final long serialVersionUID = 1L;
	
	/** 档位名称 */
	private String name;
	
	/** 视频码率：一般设备的通道1用这个码率 */
	private String videoBitRate;
	
	/** 备用视频码率：一般设备除通道1以外的通道用这个码率 */
	private String videoBitRateSpare;
	
	/** 视频分辨率：一般设备的通道1用这个分辨率 */
	private String videoResolution;
	
	/** 备用视频分辨率：一般设备除通道1以外的通道用这个分辨率 */
	private String videoResolutionSpare;
	
	/** 音频码率 */
	private String audioBitRate;
	
	/** 档位 */
	private String level;

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
			.setAudioBitRate(entity.getAudioBitRate())
			.setLevel(entity.getLevel()==null?"":entity.getLevel().getName());
		
		return this;
	}
	
	
}
