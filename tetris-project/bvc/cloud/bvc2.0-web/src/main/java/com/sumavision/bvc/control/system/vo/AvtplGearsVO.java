package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @className av参数档位
 * @author Administrator
 * @date 2018年7月30日 上午9:20
 */
public class AvtplGearsVO extends AbstractBaseVO<AvtplGearsVO, AvtplGearsPO>{

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
	
	/** 视频帧率 */
	private String fps;
	
	/** 音频码率 */
	private String audioBitRate;
	
	/** 档位 */
	private String level;

	public String getName() {
		return name;
	}

	public AvtplGearsVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoBitRate() {
		return videoBitRate;
	}

	public AvtplGearsVO setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
		return this;
	}

	public String getVideoBitRateSpare() {
		return videoBitRateSpare;
	}

	public AvtplGearsVO setVideoBitRateSpare(String videoBitRateSpare) {
		this.videoBitRateSpare = videoBitRateSpare;
		return this;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public AvtplGearsVO setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
		return this;
	}

	public String getVideoResolutionSpare() {
		return videoResolutionSpare;
	}

	public AvtplGearsVO setVideoResolutionSpare(String videoResolutionSpare) {
		this.videoResolutionSpare = videoResolutionSpare;
		return this;
	}

	public String getFps() {
		return fps;
	}

	public AvtplGearsVO setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public String getAudioBitRate() {
		return audioBitRate;
	}

	public AvtplGearsVO setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
		return this;
	}

	public String getLevel() {
		return level;
	}

	public AvtplGearsVO setLevel(String level) {
		this.level = level;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public AvtplGearsVO set(AvtplGearsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoBitRate(entity.getVideoBitRate())
			.setVideoResolution(entity.getVideoResolution().getName())
			.setVideoBitRateSpare(entity.getVideoBitRateSpare())
			.setVideoResolutionSpare(entity.getVideoResolutionSpare().getName())
			.setFps(entity.getFps())
			.setAudioBitRate(entity.getAudioBitRate())
			.setLevel(entity.getLevel()==null?"":entity.getLevel().getName());
		return this;
	}
	
	
	 
	 
}
