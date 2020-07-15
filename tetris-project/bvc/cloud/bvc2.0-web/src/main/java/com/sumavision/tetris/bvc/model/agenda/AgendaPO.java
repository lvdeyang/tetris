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
	
	/** 设备议程音频 */
	private Integer volume = 100;
	
	/** 音频处理方式 */
	private AudioOperationType audioOperationType;

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

	@Column(name = "VOLUME")
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	@Column(name = "AUDIO_OPERATION_TYPE")
	@Enumerated(value = EnumType.STRING)
	public AudioOperationType getAudioOperationType() {
		return audioOperationType;
	}

	public void setAudioOperationType(AudioOperationType audioOperationType) {
		this.audioOperationType = audioOperationType;
	}
	
}
