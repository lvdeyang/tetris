package com.sumavision.tetris.bvc.model.terminal.screen;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalScreenVO extends AbstractBaseVO<TerminalScreenVO, TerminalScreenPO>{

	private String name;
	
	private String screenPrimaryKey;
	
	private Long terminalId;
	
	private Long terminalChannelId;
	
	private String terminalChannelName;
	
	public String getName() {
		return name;
	}

	public TerminalScreenVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public TerminalScreenVO setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalScreenVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public TerminalScreenVO setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
		return this;
	}

	public String getTerminalChannelName() {
		return terminalChannelName;
	}

	public TerminalScreenVO setTerminalChannelName(String terminalChannelName) {
		this.terminalChannelName = terminalChannelName;
		return this;
	}

	@Override
	public TerminalScreenVO set(TerminalScreenPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setScreenPrimaryKey(entity.getScreenPrimaryKey())
			.setTerminalId(entity.getTerminalId())
			.setTerminalChannelId(entity.getTerminalChannelId());
		return this;
	}

}
