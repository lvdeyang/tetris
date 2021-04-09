package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_FORWARD_DESTINATION")
public class AgendaForwardDestinationPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 目的id */
	private Long destinationId;
	
	/** 目的类型 */
	private DestinationType destinationType;
	
	/** 议程转发id */
	private Long agendaForwardId;

	@Column(name = "DESTINATION_ID")
	public Long getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DESTINATION_TYPE")
	public DestinationType getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(DestinationType destinationType) {
		this.destinationType = destinationType;
	}

	@Column(name = "AGENDA_FORWARD_ID")
	public Long getAgendaForwardId() {
		return agendaForwardId;
	}

	public void setAgendaForwardId(Long agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
	}
	
}
