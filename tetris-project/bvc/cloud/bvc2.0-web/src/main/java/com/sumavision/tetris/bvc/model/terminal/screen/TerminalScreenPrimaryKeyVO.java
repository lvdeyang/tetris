package com.sumavision.tetris.bvc.model.terminal.screen;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalScreenPrimaryKeyVO extends AbstractBaseVO<TerminalScreenPrimaryKeyVO, TerminalScreenPrimaryKeyPO>{

	private String name;
	
	private String screenPrimaryKey;
	
	public String getName() {
		return name;
	}

	public TerminalScreenPrimaryKeyVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public TerminalScreenPrimaryKeyVO setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
		return this;
	}

	@Override
	public TerminalScreenPrimaryKeyVO set(TerminalScreenPrimaryKeyPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setScreenPrimaryKey(entity.getScreenPrimaryKey());
		return this;
	}
	
}
