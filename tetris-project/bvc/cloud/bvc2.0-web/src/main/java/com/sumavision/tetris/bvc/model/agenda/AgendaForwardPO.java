package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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
	
	/** 业务类型 */
	private BusinessInfoType businessInfoType;

	/** 转发类型 */
	private AgendaForwardType type;
	
	/** 视频源id bundleId-channelId*/
	private String sourceId;
	
	/** 源类型 */
	private AgendaSourceType sourceType;
	
	/** 音频源id */
	private String audioSourceId;
	
	/** 音频源类型 */
	private AgendaSourceType audioSourceType;
	
	/** 目的id */
	private String destinationId;
	
	/** 目的类型 */
	private AgendaDestinationType destinationType;
	
	/** 隶属议程id */
	private Long agendaId;
	
	@Column(name = "BUSINESS_INFO_TYPE")
	@Enumerated(value = EnumType.STRING)
	public BusinessInfoType getBusinessInfoType() {
		return businessInfoType;
	}

	public void setBusinessInfoType(BusinessInfoType businessInfoType) {
		this.businessInfoType = businessInfoType;
	}

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public AgendaForwardType getType() {
		return type;
	}

	public void setType(AgendaForwardType type) {
		this.type = type;
	}

	@Column(name = "SOURCE_ID")
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "SOURCE_TYPE")
	@Enumerated(value = EnumType.STRING)
	public AgendaSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(AgendaSourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "AUDIO_SOURCE_ID")
	public String getAudioSourceId() {
		return audioSourceId;
	}

	public void setAudioSourceId(String audioSourceId) {
		this.audioSourceId = audioSourceId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_SOURCE_TYPE")
	public AgendaSourceType getAudioSourceType() {
		return audioSourceType;
	}

	public void setAudioSourceType(AgendaSourceType audioSourceType) {
		this.audioSourceType = audioSourceType;
	}

	@Column(name = "DESTINATION_ID")
	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	@Column(name = "DESTINATION_TYPE")
	@Enumerated(value = EnumType.STRING)
	public AgendaDestinationType getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(AgendaDestinationType destinationType) {
		this.destinationType = destinationType;
	}

	@Column(name = "AGENDA_ID")
	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}
	
}
