package com.sumavision.bvc.common.group.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.AudioOperationType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 会议配置（议程【|方案】）
 * @author lvdeyang
 * @date 2018年8月4日 上午10:20:11 
 */
@Entity
@Table(name="BVC_COMMON_CONFIG")
public class CommonConfigPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 议程名称 */
	private String name;
	
	/** 议程描述 */
	private String remark;
	
	/** 配置类型 */
	private ConfigType type;
	
	/** ConfigType.AGENDA 时记录哪个议程执行 */
	private Boolean run;
	
	/** 设备议程音频 */
	private int volume = 100;
	
	/** 音频操作方式 */
	private AudioOperationType audioOperation;
	
	/** 配置中的音频 */
	private Set<CommonConfigAudioPO> audios;
	
	/** 配置中的视频 */
	private Set<CommonConfigVideoPO> videos;
	
	/** 关联设备组 */
	private CommonGroupPO group;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}

	@Column(name = "RUN")
	public Boolean getRun() {
		return run;
	}

	public void setRun(Boolean run) {
		this.run = run;
	}

	@Column(name = "VOLUME")
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_TYPE")
	public AudioOperationType getAudioOperation() {
		return audioOperation;
	}

	public void setAudioOperation(AudioOperationType audioOperation) {
		this.audioOperation = audioOperation;
	}
	
	@OneToMany(mappedBy = "config", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonConfigAudioPO> getAudios() {
		return audios;
	}

	public void setAudios(Set<CommonConfigAudioPO> audios) {
		this.audios = audios;
	}
	
	@OneToMany(mappedBy = "config", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonConfigVideoPO> getVideos() {
		return videos;
	}

	public void setVideos(Set<CommonConfigVideoPO> videos) {
		this.videos = videos;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public CommonGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommonGroupPO group) {
		this.group = group;
	}
	
}
