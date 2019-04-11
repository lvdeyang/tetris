package com.sumavision.tetris.cs.program;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProgramVO extends AbstractBaseVO<ProgramVO,ProgramPO>{
	
	private Long channelId;
	private Long screenNum;
	private List<ScreenVO> screenInfo;

	@Override
	public ProgramVO set(ProgramPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setChannelId(entity.getChannelId())
		.setScreenNum(entity.getScreenNum());
		
		return this;
	}

	public Long getChannelId() {
		return channelId;
	}

	public ProgramVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public Long getScreenNum() {
		return screenNum;
	}

	public ProgramVO setScreenNum(Long screenNum) {
		this.screenNum = screenNum;
		return this;
	}

	public List<ScreenVO> getScreenInfo() {
		return screenInfo;
	}

	public void setScreenInfo(List<ScreenVO> screenInfo) {
		this.screenInfo = screenInfo;
	}
}
