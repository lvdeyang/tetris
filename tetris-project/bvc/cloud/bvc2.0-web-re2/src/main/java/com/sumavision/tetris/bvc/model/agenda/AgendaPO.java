package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:27:34
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA")
public class AgendaPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 名称 */
	private String name;

	/** 描述 */
	private String remark;
	
	/** 隶属业务id */
	private Long businessId;
	
	/** 业务类型 */
	private BusinessInfoType businessInfoType;
	
	/** 音频优先级   自动1：1角色混音*/
	private AudioPriority audioPriority;
	
	/** 是否启用全局音频     使用议程音频还是议程转发音频*/
	private Boolean globalCustomAudio;
	
	/** 记录当前议程音频的处理方式 */
	private AudioType audioType;
	
	/** 音量 */
	private Integer volume;
	
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

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "BUSINESS_INFO_TYPE")
	@Enumerated(value = EnumType.STRING)
	public BusinessInfoType getBusinessInfoType() {
		return businessInfoType;
	}

	public void setBusinessInfoType(BusinessInfoType businessInfoType) {
		this.businessInfoType = businessInfoType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_PRIORITY")
	public AudioPriority getAudioPriority() {
		return audioPriority;
	}

	public void setAudioPriority(AudioPriority audioPriority) {
		this.audioPriority = audioPriority;
	}

	@Column(name = "GLOBAL_CUSTOM_AUDIO")
	public Boolean getGlobalCustomAudio() {
		return globalCustomAudio;
	}

	public void setGlobalCustomAudio(Boolean globalCustomAudio) {
		this.globalCustomAudio = globalCustomAudio;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_TYPE")
	public AudioType getAudioType() {
		return audioType;
	}

	public void setAudioType(AudioType audioType) {
		this.audioType = audioType;
	}

	@Column(name = "VOLUME")
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

}
