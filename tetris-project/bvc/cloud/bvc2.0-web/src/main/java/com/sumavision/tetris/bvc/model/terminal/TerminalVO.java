package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalVO extends AbstractBaseVO<TerminalVO, TerminalPO>{

	private String name;
	
	private String type;
	
	private String typeName;
	
	public String getName() {
		return name;
	}

	public TerminalVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public TerminalVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	@Override
	public TerminalVO set(TerminalPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType()==null?"":entity.getType().toString())
			.setTypeName(entity.getType()==null?"":entity.getType().getName());
		return this;
	}

}
