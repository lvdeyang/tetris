package com.sumavision.tetris.cms.classify;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ClassifyVO extends AbstractBaseVO<ClassifyVO, ClassifyPO>{

	private String name;
	
	private String remark;
	
	@Override
	public ClassifyVO set(ClassifyPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark());
		
		return this;
	}

	public String getName() {
		return name;
	}

	public ClassifyVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ClassifyVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

}
