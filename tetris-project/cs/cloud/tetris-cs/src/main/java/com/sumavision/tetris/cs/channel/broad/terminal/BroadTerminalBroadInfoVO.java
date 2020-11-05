package com.sumavision.tetris.cs.channel.broad.terminal;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class BroadTerminalBroadInfoVO extends AbstractBaseVO<BroadTerminalBroadInfoVO, BroadTerminalBroadInfoPO>{

	private Long channelId;
	private String level;
	private Boolean hasFile;
	
	@Override
	public BroadTerminalBroadInfoVO set(BroadTerminalBroadInfoPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setChannelId(entity.getChannelId())
				.setLevel(entity.getLevel())
				.setHasFile(entity.getHasFile());
	}

	public Long getChannelId() {
		return channelId;
	}

	public BroadTerminalBroadInfoVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getLevel() {
		return level;
	}

	public BroadTerminalBroadInfoVO setLevel(String level) {
		this.level = level;
		return this;
	}

	public Boolean getHasFile() {
		return hasFile;
	}

	public BroadTerminalBroadInfoVO setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
		return this;
	}
}
