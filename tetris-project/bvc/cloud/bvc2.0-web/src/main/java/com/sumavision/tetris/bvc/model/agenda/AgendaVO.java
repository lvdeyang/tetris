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
	
	private String businessInfoType;
	
	private String businessInfoTypeName;
	
	private String agendaModeType;
	
	private String agendaModeTypeName;
	
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

	public String getBusinessInfoType() {
		return businessInfoType;
	}

	public AgendaVO setBusinessInfoType(String businessInfoType) {
		this.businessInfoType = businessInfoType;
		return this;
	}

	public String getBusinessInfoTypeName() {
		return businessInfoTypeName;
	}

	public AgendaVO setBusinessInfoTypeName(String businessInfoTypeName) {
		this.businessInfoTypeName = businessInfoTypeName;
		return this;
	}

	public String getAgendaModeType() {
		return agendaModeType;
	}

	public AgendaVO setAgendaModeType(String agendaModeType) {
		this.agendaModeType = agendaModeType;
		return this;
	}

	public String getAgendaModeTypeName() {
		return agendaModeTypeName;
	}

	public AgendaVO setAgendaModeTypeName(String agendaModeTypeName) {
		this.agendaModeTypeName = agendaModeTypeName;
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
			.setAudioOperationTypeName(entity.getAudioOperationType().getName())
			.setBusinessInfoType(entity.getBusinessInfoType()==null?"":entity.getBusinessInfoType().toString())
			.setBusinessInfoTypeName(entity.getBusinessInfoType()==null?"":entity.getBusinessInfoType().getName())
			.setAgendaModeType(entity.getAgendaModeType()==null?"":entity.getAgendaModeType().toString())
			.setAgendaModeTypeName(entity.getAgendaModeType()==null?"":entity.getAgendaModeType().getName());
		return this;
	}

}