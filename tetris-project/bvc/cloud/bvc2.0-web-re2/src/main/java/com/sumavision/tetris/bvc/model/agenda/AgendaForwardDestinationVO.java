package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardDestinationVO extends AbstractBaseVO<AgendaForwardDestinationVO, AgendaForwardDestinationPO>{

	private Long destinationId;
	
	private String destinationName;
	
	private String destinationType;
	
	private String destinationTypeName;

	public Long getDestinationId() {
		return destinationId;
	}

	public AgendaForwardDestinationVO setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
		return this;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public AgendaForwardDestinationVO setDestinationName(String destinationName) {
		this.destinationName = destinationName;
		return this;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public AgendaForwardDestinationVO setDestinationType(String destinationType) {
		this.destinationType = destinationType;
		return this;
	}

	public String getDestinationTypeName() {
		return destinationTypeName;
	}

	public AgendaForwardDestinationVO setDestinationTypeName(String destinationTypeName) {
		this.destinationTypeName = destinationTypeName;
		return this;
	}

	@Override
	public AgendaForwardDestinationVO set(AgendaForwardDestinationPO entity) throws Exception {
		this.setId(entity.getId())
			.setDestinationId(entity.getDestinationId())
			.setDestinationType(entity.getDestinationType().toString())
			.setDestinationTypeName(entity.getDestinationType().getName());
		return this;
	}
	
}
