package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import com.sumavision.tetris.bvc.model.role.RoleVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaVO extends AbstractBaseVO<AgendaVO, AgendaPO>{

	private String name;

	private String remark;
	
	private Boolean isRun;
	
	private Long businessId;
	
	private String businessInfoType;
	
	private String businessInfoTypeName;
	
	private String audioPriority;
	
	private String audioPriorityName;
	
	private Boolean globalCustomAudio;
	
	private String audioType;
	
	private String audioTypeName;
	
	private Integer volume;
	
	private List<RoleVO> roles;
	
	private List<CustomAudioVO> customAudios;
	
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

	public Boolean getIsRun() {
		return isRun;
	}

	public AgendaVO setIsRun(Boolean isRun) {
		this.isRun = isRun;
		return this;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public AgendaVO setBusinessId(Long businessId) {
		this.businessId = businessId;
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

	public String getAudioPriority() {
		return audioPriority;
	}

	public AgendaVO setAudioPriority(String audioPriority) {
		this.audioPriority = audioPriority;
		return this;
	}

	public String getAudioPriorityName() {
		return audioPriorityName;
	}

	public AgendaVO setAudioPriorityName(String audioPriorityName) {
		this.audioPriorityName = audioPriorityName;
		return this;
	}

	public Boolean getGlobalCustomAudio() {
		return globalCustomAudio;
	}

	public AgendaVO setGlobalCustomAudio(Boolean globalCustomAudio) {
		this.globalCustomAudio = globalCustomAudio;
		return this;
	}

	public String getAudioType() {
		return audioType;
	}

	public AgendaVO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public String getAudioTypeName() {
		return audioTypeName;
	}

	public AgendaVO setAudioTypeName(String audioTypeName) {
		this.audioTypeName = audioTypeName;
		return this;
	}

	public Integer getVolume() {
		return volume;
	}

	public AgendaVO setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public AgendaVO setRoles(List<RoleVO> roles) {
		this.roles = roles;
		return this;
	}
	
	public List<CustomAudioVO> getCustomAudios() {
		return customAudios;
	}

	public AgendaVO setCustomAudios(List<CustomAudioVO> customAudios) {
		this.customAudios = customAudios;
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
			.setBusinessInfoType(entity.getBusinessInfoType()==null?"":entity.getBusinessInfoType().toString())
			.setBusinessInfoTypeName(entity.getBusinessInfoType()==null?"":entity.getBusinessInfoType().getName())
			.setAudioPriority(entity.getAudioPriority()==null?null:entity.getAudioPriority().toString())
			.setAudioPriorityName(entity.getAudioPriority()==null?null:entity.getAudioPriority().getName())
			.setGlobalCustomAudio(entity.getGlobalCustomAudio())
			.setAudioType(entity.getAudioType()==null?null:entity.getAudioType().toString())
			.setAudioTypeName(entity.getAudioType()==null?null:entity.getAudioType().getName())
			.setVolume(entity.getVolume());
		return this;
	}

}
