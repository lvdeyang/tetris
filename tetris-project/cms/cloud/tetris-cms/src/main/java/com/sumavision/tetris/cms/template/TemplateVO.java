package com.sumavision.tetris.cms.template;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TemplateVO extends AbstractBaseVO<TemplateVO, TemplatePO>{

	private String name;
	
	private String remark;
	
	private String type;
	
	public String getName() {
		return name;
	}

	public TemplateVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public TemplateVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getType() {
		return type;
	}

	public TemplateVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public TemplateVO set(TemplatePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark())
			.setType(entity.getType().getName());
		return this;
	}

}
