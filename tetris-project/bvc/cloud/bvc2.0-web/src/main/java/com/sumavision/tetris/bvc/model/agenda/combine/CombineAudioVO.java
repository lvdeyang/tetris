package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineAudioVO extends AbstractBaseVO<CombineAudioVO, CombineAudioPO>{

	private String name;
	
	private Long businessId;
	
	private String businessType;
	
	private String businessTypeName;

	public String getName() {
		return name;
	}

	public CombineAudioVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public CombineAudioVO setBusinessId(Long businessId) {
		this.businessId = businessId;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public CombineAudioVO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public CombineAudioVO setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
		return this;
	}

	@Override
	public CombineAudioVO set(CombineAudioPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setBusinessId(entity.getBusinessId())
			.setBusinessType(entity.getBusinessType()==null?null:entity.getBusinessType().toString())
			.setBusinessTypeName(entity.getBusinessType()==null?null:entity.getBusinessType().getName());
		return this;
	}
	
}
