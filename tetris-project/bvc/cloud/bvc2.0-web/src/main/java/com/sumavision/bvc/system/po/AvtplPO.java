package com.sumavision.bvc.system.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 参数模板 <br/>
 * @Description: 用来描述流调时的参数，每个模板包含三挡可降档参数（如码率分辨率等）<br/>
 * @author lvdeyang
 * @date 2018年7月24日 下午2:39:46 
 */
@Entity
@Table(name="BVC_SYSTEM_AVTPL")
public class AvtplPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 模板名称 */
	private String name;
	
	/** 视频编码格式：一般设备通道1用这个编码格式 */
	private VideoFormat videoFormat;
	
	/** 备用视频编码格式 ：一般设备除通道1以外的通道用这个编码格式*/
	private VideoFormat videoFormatSpare;
	
	/** 音频编码格式 */
	private AudioFormat audioFormat;
	 
	/** 用于标注模板用途 */
	private AvtplUsageType usageType;
	
	/** 是否启用端口复用 */
	private Boolean mux;
	
	private Set<AvtplGearsPO> gears;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_FORMAT")
	public VideoFormat getVideoFormat() {
		return videoFormat;
	}
	
	public void setVideoFormat(VideoFormat videoFormat) {
		this.videoFormat = videoFormat;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_FORMAT_SPARE")
	public VideoFormat getVideoFormatSpare() {
		return videoFormatSpare;
	}
	
	public void setVideoFormatSpare(VideoFormat videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_FORMAT")
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "USAGE_TYPE")
	public AvtplUsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(AvtplUsageType usageType) {
		this.usageType = usageType;
	}

	@Column(name = "MUX")
	public Boolean getMux() {
		return mux;
	}

	public void setMux(Boolean mux) {
		this.mux = mux;
	}

	@OneToMany(mappedBy = "avtpl", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	public Set<AvtplGearsPO> getGears() {
		return gears;
	}

	public void setGears(Set<AvtplGearsPO> gears) {
		this.gears = gears;
	}
	
	
}
