package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardSourceVO extends AbstractBaseVO<AgendaForwardSourceVO, AgendaForwardSourcePO>{

	private Long sourceId;
	
	private String sourceName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private Integer serialNum;
	
	private String layoutPositionSelectionType;
	
	private String layoutPositionSelectionTypeName;
	
	private Boolean isLoop;
	
	private Integer loopTime;
	
	public Long getSourceId() {
		return sourceId;
	}

	public AgendaForwardSourceVO setSourceId(Long sourceId) {
		this.sourceId = sourceId;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public AgendaForwardSourceVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public AgendaForwardSourceVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public AgendaForwardSourceVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public Integer getSerialNum() {
		return serialNum;
	}

	public AgendaForwardSourceVO setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public String getLayoutPositionSelectionType() {
		return layoutPositionSelectionType;
	}

	public AgendaForwardSourceVO setLayoutPositionSelectionType(String layoutPositionSelectionType) {
		this.layoutPositionSelectionType = layoutPositionSelectionType;
		return this;
	}

	public String getLayoutPositionSelectionTypeName() {
		return layoutPositionSelectionTypeName;
	}

	public AgendaForwardSourceVO setLayoutPositionSelectionTypeName(String layoutPositionSelectionTypeName) {
		this.layoutPositionSelectionTypeName = layoutPositionSelectionTypeName;
		return this;
	}

	public Boolean getIsLoop() {
		return isLoop;
	}

	public AgendaForwardSourceVO setIsLoop(Boolean isLoop) {
		this.isLoop = isLoop;
		return this;
	}

	public Integer getLoopTime() {
		return loopTime;
	}

	public AgendaForwardSourceVO setLoopTime(Integer loopTime) {
		this.loopTime = loopTime;
		return this;
	}

	@Override
	public AgendaForwardSourceVO set(AgendaForwardSourcePO entity) throws Exception {
		this.setId(entity.getId())
			.setSourceId(entity.getSourceId())
			.setSourceType(entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType().getName())
			.setSerialNum(entity.getSerialNum())
			.setLayoutPositionSelectionType(entity.getLayoutPositionSelectionType().toString())
			.setLayoutPositionSelectionTypeName(entity.getLayoutPositionSelectionType().getName())
			.setIsLoop(entity.getIsLoop())
			.setLoopTime(entity.getLoopTime());
		return this;
	}
	
}
