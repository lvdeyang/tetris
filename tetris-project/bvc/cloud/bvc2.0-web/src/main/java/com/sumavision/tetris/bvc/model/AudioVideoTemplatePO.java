package com.sumavision.tetris.bvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流调参数表<br/>
 * <p>
 * 	1.isTemplate==true：参数模板，区分业务，创建业务时从参数模板复制成业务参数<br/>
 *  2.isTemplate==false：业务参数，与具体业务绑定
 * </p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:00:21
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AUDIO_VIDEO_TEMPLATE")
public class AudioVideoTemplatePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 模板名称 */
	private String name;
	
	/** 视频编码格式：一般设备通道1用这个编码格式 */
	private VideoFormat videoFormat;
	
	/** 备用视频编码格式 ：一般设备除通道1以外的通道用这个编码格式*/
	private VideoFormat videoFormatSpare;
	
	/** 音频编码格式 */
	private AudioFormat audioFormat;
	 
	/** 是否启用端口复用 */
	private Boolean mux;
	
	/** 视频码率：一般设备的通道1用这个码率 */
	private String videoBitRate;
	
	/** 备用视频码率：一般设备除通道1以外的通道用这个码率 */
	private String videoBitRateSpare;
	
	/** 视频分辨率：一般设备的通道1用这个分辨率 */
	private Resolution videoResolution;
	
	/** 备用视频分辨率：一般设备除通道1以外的通道用这个分辨率 */
	private Resolution videoResolutionSpare;
	
	/** 视频帧率 */
	private String fps;
	
	/** 音频码率 */
	private String audioBitRate;
	
	/** 用于标注模板用途 */
	private UsageType usageType;
	
	/** 是否是模板 */
	private Boolean isTemplate;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "VIDEO_FORMAT")
	@Enumerated(value = EnumType.STRING)
	public VideoFormat getVideoFormat() {
		return videoFormat;
	}

	public void setVideoFormat(VideoFormat videoFormat) {
		this.videoFormat = videoFormat;
	}

	@Column(name = "VIDEO_FORMAT_SPARE")
	@Enumerated(value = EnumType.STRING)
	public VideoFormat getVideoFormatSpare() {
		return videoFormatSpare;
	}

	public void setVideoFormatSpare(VideoFormat videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
	}

	@Column(name = "AUDIO_FORMAT")
	@Enumerated(value = EnumType.STRING)
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

	@Column(name = "MUX")
	public Boolean getMux() {
		return mux;
	}

	public void setMux(Boolean mux) {
		this.mux = mux;
	}

	@Column(name = "VIDEO_BIT_RATE")
	public String getVideoBitRate() {
		return videoBitRate;
	}

	public void setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
	}

	@Column(name = "VIDEO_BIT_RATE_SPARE")
	public String getVideoBitRateSpare() {
		return videoBitRateSpare;
	}

	public void setVideoBitRateSpare(String videoBitRateSpare) {
		this.videoBitRateSpare = videoBitRateSpare;
	}

	@Column(name = "VIDEO_RESOLUTION")
	@Enumerated(value = EnumType.STRING)
	public Resolution getVideoResolution() {
		return videoResolution;
	}

	public void setVideoResolution(Resolution videoResolution) {
		this.videoResolution = videoResolution;
	}

	@Column(name = "VIDEO_RESOLUTION_SPARE")
	@Enumerated(value = EnumType.STRING)
	public Resolution getVideoResolutionSpare() {
		return videoResolutionSpare;
	}

	public void setVideoResolutionSpare(Resolution videoResolutionSpare) {
		this.videoResolutionSpare = videoResolutionSpare;
	}

	@Column(name = "FPS")
	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	@Column(name = "AUDIO_BIT_RATE")
	public String getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	@Column(name = "USAGE_TYPE")
	@Enumerated(value = EnumType.STRING)
	public UsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(UsageType usageType) {
		this.usageType = usageType;
	}

	@Column(name = "IS_TEMPLATE")
	public Boolean getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	
}
