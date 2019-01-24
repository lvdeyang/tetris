package com.sumavision.tetris.organization;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CompanyVO extends AbstractBaseVO<CompanyVO, CompanyPO>{

	private String name;
	
	public String getName() {
		return name;
	}

	public CompanyVO setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public CompanyVO set(CompanyPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}

}
