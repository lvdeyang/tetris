package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaVO extends AbstractBaseVO<AgendaVO, AgendaPO>{

	private String name;

	private String remark;
	
	private Long businessId;
	
	private Integer volume = 100;
	
	private String audioOperationType;
	
	private String audioOperationTypeName;
	
	public String getName() {
		return name;
	}

	public AgendaVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public AgendaVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public AgendaVO setBusinessId(Long businessId) {
		this.businessId = businessId;
		return this;
	}

	public Integer getVolume() {
		return volume;
	}

	public AgendaVO setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}

	public String getAudioOperationType() {
		return audioOperationType;
	}

	public AgendaVO setAudioOperationType(String audioOperationType) {
		this.audioOperationType = audioOperationType;
		return this;
	}

	public String getAudioOperationTypeName() {
		return audioOperationTypeName;
	}

	public AgendaVO setAudioOperationTypeName(String audioOperationTypeName) {
		this.audioOperationTypeName = audioOperationTypeName;
		return this;
	}

	@Override
	public AgendaVO set(AgendaPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark())
			.setBusinessId(entity.getBusinessId())
			.setVolume(entity.getVolume())
			.setAudioOperationType(entity.getAudioOperationType().toString())
			.setAudioOperationTypeName(entity.getAudioOperationType().getName());
		return this;
	}

}
