package com.sumavision.tetris.bvc.model.layout;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LayoutVO extends AbstractBaseVO<LayoutVO, LayoutPO>{

	private String name;
	
	private List<LayoutPositionVO> positions;
	
	public String getName() {
		return name;
	}

	public LayoutVO setName(String name) {
		this.name = name;
		return this;
	}

	public List<LayoutPositionVO> getPositions() {
		return positions;
	}

	public LayoutVO setPositions(List<LayoutPositionVO> positions) {
		this.positions = positions;
		return this;
	}

	@Override
	public LayoutVO set(LayoutPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}

}
