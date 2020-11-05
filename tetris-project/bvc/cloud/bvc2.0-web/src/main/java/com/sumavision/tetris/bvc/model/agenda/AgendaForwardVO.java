package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardVO extends AbstractBaseVO<AgendaForwardVO, AgendaForwardPO>{

	private String type;
	
	private String typeName;
	
	private String businessInfoType;
	
	private String businessInfoTypeName;
	
	private String sourceId;
	
	private String sourceName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private String audioSourceId;
	
	private String audioSourceName;
	
	private String audioSourceType;
	
	private String audioSourceTypeName;
	
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

	public String getBusinessInfoType() {
		return businessInfoType;
	}

	public AgendaForwardVO setBusinessInfoType(String businessInfoType) {
		this.businessInfoType = businessInfoType;
		return this;
	}

	public String getBusinessInfoTypeName() {
		return businessInfoTypeName;
	}

	public AgendaForwardVO setBusinessInfoTypeName(String businessInfoTypeName) {
		this.businessInfoTypeName = businessInfoTypeName;
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

	public String getAudioSourceId() {
		return audioSourceId;
	}

	public AgendaForwardVO setAudioSourceId(String audioSourceId) {
		this.audioSourceId = audioSourceId;
		return this;
	}

	public String getAudioSourceName() {
		return audioSourceName;
	}

	public AgendaForwardVO setAudioSourceName(String audioSourceName) {
		this.audioSourceName = audioSourceName;
		return this;
	}

	public String getAudioSourceType() {
		return audioSourceType;
	}

	public AgendaForwardVO setAudioSourceType(String audioSourceType) {
		this.audioSourceType = audioSourceType;
		return this;
	}

	public String getAudioSourceTypeName() {
		return audioSourceTypeName;
	}

	public AgendaForwardVO setAudioSourceTypeName(String audioSourceTypeName) {
		this.audioSourceTypeName = audioSourceTypeName;
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
			.setBusinessInfoType(entity.getBusinessInfoType()==null?null:entity.getBusinessInfoType().toString())
			.setBusinessInfoTypeName(entity.getBusinessInfoType()==null?null:entity.getBusinessInfoType().getName())
			.setSourceId(entity.getSourceId())
			.setSourceName("-")
			.setSourceType(entity.getSourceType()==null?null:entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType()==null?"-":entity.getSourceType().getName())
			.setAudioSourceId(entity.getAudioSourceId())
			.setAudioSourceName("-")
			.setAudioSourceType(entity.getAudioSourceType()==null?null:entity.getAudioSourceType().toString())
			.setAudioSourceTypeName(entity.getAudioSourceType()==null?"-":entity.getAudioSourceType().getName())
			.setDestinationId(entity.getDestinationId())
			.setDestinationType(entity.getDestinationType().toString())
			.setDestinationTypeName(entity.getDestinationType().getName())
			.setAgendaId(entity.getAgendaId());
		return this;
	}

}
