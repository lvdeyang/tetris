package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineVideoVO extends AbstractBaseVO<CombineVideoVO, CombineVideoPO>{
	
	private String name;
	
	private String websiteDraw;
	
	private Long businessId;
	
	private String businessType;
	
	private String businessTypeName;
	
	public String getName() {
		return name;
	}

	public CombineVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public CombineVideoVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public CombineVideoVO setBusinessId(Long businessId) {
		this.businessId = businessId;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public CombineVideoVO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public CombineVideoVO setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
		return this;
	}

	@Override
	public CombineVideoVO set(CombineVideoPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setWebsiteDraw(entity.getWebsiteDraw())
			.setBusinessId(entity.getBusinessId())
			.setBusinessType(entity.getBusinessType().toString())
			.setBusinessTypeName(entity.getBusinessType().getName());
		return this;
	}

}
