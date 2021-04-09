package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LayoutScopeVO extends AbstractBaseVO<LayoutScopeVO, LayoutScopePO>{

	private Integer sourceNumber;
	
	private Long layoutId;
	
	private String layoutName;

	public Integer getSourceNumber() {
		return sourceNumber;
	}

	public LayoutScopeVO setSourceNumber(Integer sourceNumber) {
		this.sourceNumber = sourceNumber;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public LayoutScopeVO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public LayoutScopeVO setLayoutName(String layoutName) {
		this.layoutName = layoutName;
		return this;
	}

	@Override
	public LayoutScopeVO set(LayoutScopePO entity) throws Exception {
		this.setId(entity.getId())
			.setSourceNumber(entity.getSourceNumber())
			.setLayoutId(entity.getLayoutId());
		return this;
	}
	
}
