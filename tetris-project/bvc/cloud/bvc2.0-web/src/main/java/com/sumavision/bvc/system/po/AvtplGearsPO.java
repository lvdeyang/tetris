package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 参数模板的挡位<br/>
 * @Description: 每个参数模板包含三个档位<br/>
 * @author lvdeyang 
 * @date 2018年7月24日 下午2:44:20 
 */
@Entity
@Table(name = "BVC_SYSTEM_AVTPL_GEARS")
public class AvtplGearsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 档位名称 */
	private String name;
	
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
	
	/** 档位 */
	private GearsLevel level;

	private AvtplPO avtpl;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_RESOLUTION")
	public Resolution getVideoResolution() {
		return videoResolution;
	}

	public void setVideoResolution(Resolution videoResolution) {
		this.videoResolution = videoResolution;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_RESOLUTION_SPARE")
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
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "LEVEL")
	public GearsLevel getLevel() {
		return level;
	}

	public void setLevel(GearsLevel level) {
		this.level = level;
	}

	@ManyToOne
	@JoinColumn(name="AVTPL_ID")
	public AvtplPO getAvtpl() {
		return avtpl;
	}
	
	public void setAvtpl(AvtplPO avtpl) {
		this.avtpl = avtpl;
	}
	
}
