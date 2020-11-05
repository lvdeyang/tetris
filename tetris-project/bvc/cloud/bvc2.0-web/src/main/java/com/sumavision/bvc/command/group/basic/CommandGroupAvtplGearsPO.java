package com.sumavision.bvc.command.group.basic;

import java.util.Date;

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
 * 
* @ClassName: 参数档位 
* @author zy
* @date 2018年7月31日 上午10:00:03 
*
 */
@Entity
@Table(name = "BVC_COMMAND_GROUP_AVTPL_GEARS")
public class CommandGroupAvtplGearsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 档位名称 */ 
	private String name;
	
	/** 视频码率 */
    private String videoBitRate;
    
    /** 视频备用码率 */
	private String videoBitRateSpare;
	
	/** 视频分辨率 */
	private Resolution VideoResolution;
	
	/** 视频备用分辨率 */
	private Resolution VideoResolutionSpare;
	
	/** 音频码率 */
	private String AudioBitRate;
	
	/** 档位 */
	private GearsLevel level;
	
	/** 关联参数模板 */
	private CommandGroupAvtplPO avtpl;
	
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
		return VideoResolution;
	}

	public void setVideoResolution(Resolution videoResolution) {
		VideoResolution = videoResolution;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_RESOLUTION_SPARE")
	public Resolution getVideoResolutionSpare() {
		return VideoResolutionSpare;
	}

	public void setVideoResolutionSpare(Resolution videoResolutionSpare) {
		VideoResolutionSpare = videoResolutionSpare;
	}

	@Column(name = "AUDIO_BIT_RATE")
	public String getAudioBitRate() {
		return AudioBitRate;
	}
	
	public void setAudioBitRate(String audioBitRate) {
		AudioBitRate = audioBitRate;
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
	@JoinColumn(name = "AVTPL_ID")
	public CommandGroupAvtplPO getAvtpl() {
		return avtpl;
	}

	public void setAvtpl(CommandGroupAvtplPO avtpl) {
		this.avtpl = avtpl;
	}
	
	/**
	 * @Title: 从系统资源中复制数据 
	 * @param entity 系统资源
	 * @return AvtplGearsPO 设备组资源
	 */
	public CommandGroupAvtplGearsPO set(com.sumavision.bvc.system.po.AvtplGearsPO entity){
		this.setUuid(entity.getUuid());
		this.setUpdateTime(new Date());
		this.setName(entity.getName());
		this.setVideoBitRate(entity.getVideoBitRate());
		this.setVideoBitRateSpare(entity.getVideoBitRateSpare());
		this.setVideoResolution(entity.getVideoResolution());
		this.setVideoResolutionSpare(entity.getVideoResolutionSpare());
		this.setAudioBitRate(entity.getAudioBitRate());
		this.setLevel(entity.getLevel());
		return this;
	}
	
}
