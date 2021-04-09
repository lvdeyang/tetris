package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程中的转发<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:28:33
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_FORWARD")
public class AgendaForwardPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 为转发命名 */
	private String name;
	
	/** 隶属议程id */
	private Long agendaId;
	
	/** 使用的虚拟源id */
	private Long layoutId;
	
	/** 画面选择模式，如果值为CONFIRM，取layoutId，如果取值为ADAPTABLE，需要从虚拟源域中选择一个 */
	private LayoutSelectionType layoutSelectionType;
	
	/** 记录本次转发音频的处理方式 */
	private AudioType audioType;
	
	/** 音量 */
	private Integer volume;
	
	/** 非组业务时使用，如指挥的媒体转发,是GroupPO的id */
	private Long businessId;
	
	/** businessId有值才有值，其他类型的转发*/
	private BusinessInfoType agendaForwardBusinessType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AGENDA_ID")
	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "LAYOUT_SELECTION_TYPE")
	public LayoutSelectionType getLayoutSelectionType() {
		return layoutSelectionType;
	}

	public void setLayoutSelectionType(LayoutSelectionType layoutSelectionType) {
		this.layoutSelectionType = layoutSelectionType;
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

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AGENDA_FORWARD_BUSINESS_TYPE")
	public BusinessInfoType getAgendaForwardBusinessType() {
		return agendaForwardBusinessType;
	}

	public void setAgendaForwardBusinessType(BusinessInfoType agendaForwardBusinessType) {
		this.agendaForwardBusinessType = agendaForwardBusinessType;
	}
	
}
