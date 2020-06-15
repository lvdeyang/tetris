package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalVO extends AbstractBaseVO<TerminalVO, TerminalPO>{

	private String name;
	
	public String getName() {
		return name;
	}

	public TerminalVO setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TerminalVO set(TerminalPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}

}
