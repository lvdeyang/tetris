package com.sumavision.tetris.bvc.model.terminal.audio;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalAudioOutputVO extends AbstractBaseVO<TerminalAudioOutputVO, TerminalAudioOutputPO>{

	private String name;
	
	private Long terminalId;
	
	public String getName() {
		return name;
	}

	public TerminalAudioOutputVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public TerminalAudioOutputVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	@Override
	public TerminalAudioOutputVO set(TerminalAudioOutputPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName());
		return this;
	}
	
}
