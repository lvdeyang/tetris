package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalBundleChannelVO extends AbstractBaseVO<TerminalBundleChannelVO, TerminalBundleChannelPO>{

	private String channelId;

	private String type;
	
	private Long terminalBundleId;
	
	public String getChannelId() {
		return channelId;
	}

	public TerminalBundleChannelVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalBundleChannelVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public TerminalBundleChannelVO setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
		return this;
	}

	@Override
	public TerminalBundleChannelVO set(TerminalBundleChannelPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setChannelId(entity.getChannelId())
			.setType(entity.getType().toString())
			.setTerminalBundleId(entity.getTerminalBundleId());
		return this;
	}

}
