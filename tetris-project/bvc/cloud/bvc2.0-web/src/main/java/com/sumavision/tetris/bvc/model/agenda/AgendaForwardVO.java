package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardVO extends AbstractBaseVO<AgendaForwardVO, AgendaForwardPO>{

	private String type;
	
	private String typeName;
	
	private String sourceId;
	
	private String sourceName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private String destinationId;
	
	private String destinationName;
	
	private String destinationType;
	
	private String destinationTypeName;
	
	private Long agendaId;
	
	public String getType() {
		return type;
	}

	public AgendaForwardVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public AgendaForwardVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public String getSourceId() {
		return sourceId;
	}

	public AgendaForwardVO setSourceId(String sourceId) {
		this.sourceId = sourceId;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public AgendaForwardVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public AgendaForwardVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public AgendaForwardVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public AgendaForwardVO setDestinationId(String destinationId) {
		this.destinationId = destinationId;
		return this;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public AgendaForwardVO setDestinationName(String destinationName) {
		this.destinationName = destinationName;
		return this;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public AgendaForwardVO setDestinationType(String destinationType) {
		this.destinationType = destinationType;
		return this;
	}

	public String getDestinationTypeName() {
		return destinationTypeName;
	}

	public AgendaForwardVO setDestinationTypeName(String destinationTypeName) {
		this.destinationTypeName = destinationTypeName;
		return this;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public AgendaForwardVO setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
		return this;
	}

	@Override
	public AgendaForwardVO set(AgendaForwardPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setSourceId(entity.getSourceId())
			.setSourceType(entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType().getName())
			.setDestinationId(entity.getDestinationId())
			.setDestinationType(entity.getDestinationType().toString())
			.setDestinationTypeName(entity.getDestinationType().getName())
			.setAgendaId(entity.getAgendaId());
		return this;
	}

}
