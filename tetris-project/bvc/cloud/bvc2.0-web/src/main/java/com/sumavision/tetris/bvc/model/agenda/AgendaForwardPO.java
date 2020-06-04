package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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

	/** 转发类型 */
	private AgendaForwardType type;
	
	/** 源id */
	private Long sourceId;
	
	/** 源类型 */
	private AgendaSourceType sourceType;
	
	/** 目的id */
	private Long destinationId;
	
	/** 目的类型 */
	private AgendaDestinationType destinationType;
	
	/** 隶属议程id */
	private Long agendaId;

	@Column(name = "TYPE")
	public AgendaForwardType getType() {
		return type;
	}

	public void setType(AgendaForwardType type) {
		this.type = type;
	}

	@Column(name = "SOURCE_ID")
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
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

	@Column(name = "DESTINATION_ID")
	public Long getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Long destinationId) {
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
